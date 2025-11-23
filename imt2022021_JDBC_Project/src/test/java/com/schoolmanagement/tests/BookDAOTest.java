package com.schoolmanagement.tests;

import com.schoolmanagement.dao.BookDAO;
import com.schoolmanagement.models.Book;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest {

    private static Connection connection;
    private static BookDAO bookDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        bookDAO = new BookDAO(connection);

        // Ensure library + course rows exist for FK
        try (Statement st = connection.createStatement()) {
            st.execute("INSERT IGNORE INTO libraries (id, name) VALUES (1, 'Main Library')");
            st.execute("INSERT IGNORE INTO courses (course_id, course_code, course_name, course_description) VALUES (1, 'C001', 'Algorithms', 'Test Course')");
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null) connection.close();
    }

    @BeforeEach
    void cleanTablesBefore() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM course_books");
            st.execute("DELETE FROM books");
        }
    }

    @Test
    void testCreateAndReadAndDelete() throws SQLException {
        // create book using DAO (ID auto-generated)
        Book b = new Book(0, "B10", "Test Title", "Author X", 1);
        bookDAO.create(b);

        // fetch the inserted row using book_id
        int generatedId = fetchIdByBookCode("B10");
        assertTrue(generatedId > 0);

        Book read = bookDAO.read(generatedId);
        assertNotNull(read);
        assertEquals("Test Title", read.getTitle());

        // delete and verify removed
        bookDAO.delete(generatedId);
        assertNull(bookDAO.read(generatedId));
    }

    @Test
    void testUpdateChangesRow() throws SQLException {
        // insert manually with auto_increment
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO books (book_id, title, author, library_id) VALUES (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, "B11");
            ps.setString(2, "Old Title");
            ps.setString(3, "A");
            ps.setInt(4, 1);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            assertTrue(keys.next());
        }

        int id = fetchIdByBookCode("B11");

        // update via DAO (correct argument order)
        bookDAO.update("B11", "New Title", "A");

        // verify update persisted
        Book updated = bookDAO.read(id);
        assertNotNull(updated);
        assertEquals("New Title", updated.getTitle());
    }

    @Test
    void testMarkAndUnmarkBookWithCourse_affectsDB() throws SQLException {

        // create book
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO books (book_id, title, author, library_id) VALUES (?, ?, ?, ?)"
        )) {
            ps.setString(1, "B12");
            ps.setString(2, "Course Book");
            ps.setString(3, "A");
            ps.setInt(4, 1);
            ps.executeUpdate();
        }

        int id = fetchIdByBookCode("B12");

        // mark book with course
        bookDAO.markBookWithCourse(id, 1);

        // verify
        try (PreparedStatement check = connection.prepareStatement(
            "SELECT * FROM course_books WHERE course_id = ? AND book_id = ?"
        )) {
            check.setInt(1, 1);
            check.setInt(2, id);
            ResultSet rs = check.executeQuery();
            assertTrue(rs.next());
        }

        // unmark
        bookDAO.unmarkBookWithCourse(id, 1);

        // verify removed
        try (PreparedStatement check = connection.prepareStatement(
            "SELECT * FROM course_books WHERE course_id = ? AND book_id = ?"
        )) {
            check.setInt(1, 1);
            check.setInt(2, id);
            ResultSet rs = check.executeQuery();
            assertFalse(rs.next());
        }
    }

    @Test
    void testDeleteNonExistingBook_doesNotThrow() {
        assertDoesNotThrow(() -> bookDAO.delete(999999));
    }

    @Test
    void testUpdate_nonExistingBook_doesNotThrow() {
        assertDoesNotThrow(() -> bookDAO.update("New", "NO_ID", "A"));
    }

    @Test
    void testMapResultSetToList_empty() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM books WHERE id = -1");
        List<Book> list = bookDAO.mapResultSetToList(rs);
        assertNotNull(list);
        assertEquals(0, list.size());
    }


    @Test
    void testGetBooksForCourse_returnsList() throws SQLException {
        // create book
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO books (book_id, title, author, library_id) VALUES (?, ?, ?, ?)"
        )) {
            ps.setString(1, "B20");
            ps.setString(2, "CourseLinked");
            ps.setString(3, "X");
            ps.setInt(4, 1);
            ps.executeUpdate();
        }

        int id = fetchIdByBookCode("B20");

        // link to course
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO course_books (course_id, book_id) VALUES (?, ?)"
        )) {
            ps.setInt(1, 1);
            ps.setInt(2, id);
            ps.executeUpdate();
        }

        var list = bookDAO.getBooksForCourse(1);
        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }

    @Test
    void testReadNonExistent_returnsNull() throws SQLException {
        assertNull(bookDAO.read(-9999));
    }

    // Utility method to fetch row ID from book_id
    private int fetchIdByBookCode(String code) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT id FROM books WHERE book_id = ?"
        )) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }
}
