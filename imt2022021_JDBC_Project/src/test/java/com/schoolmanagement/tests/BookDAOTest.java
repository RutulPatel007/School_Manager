package com.schoolmanagement.tests;

import com.schoolmanagement.dao.BookDAO;
import com.schoolmanagement.models.Book;
import org.junit.jupiter.api.*;

import java.sql.*;

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

        // Ensure supporting rows exist
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
        // create
        Book b = new Book(10, "B10", "Test Title", "Author X", 1);
        bookDAO.create(b);

        // verify created by reading back
        Book read = bookDAO.read(10);
        assertNotNull(read);
        assertEquals("Test Title", read.getTitle());

        // delete and verify removed
        bookDAO.delete(10);
        assertNull(bookDAO.read(10));
    }

    @Test
    void testUpdateChangesRow() throws SQLException {
        // insert
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO books (id, book_id, title, author, library_id) VALUES (?,?, ?, ?, ?)")) {
            ps.setInt(1, 11);
            ps.setString(2, "B11");
            ps.setString(3, "Old Title");
            ps.setString(4, "A");
            ps.setInt(5, 1);
            ps.executeUpdate();
        }

        // update via DAO
        bookDAO.update("New Title", "B11", "A");

        // verify update persisted
        Book updated = bookDAO.read(11);
        assertNotNull(updated);
        assertEquals("New Title", updated.getTitle());

        // cleanup
        bookDAO.delete(11);
    }

    @Test
    void testMarkAndUnmarkBookWithCourse_affectsDB() throws SQLException {
        // Ensure course exists
        try (Statement st = connection.createStatement()) {
            st.execute("INSERT IGNORE INTO courses (course_id, course_code, course_name, course_description) " +
                    "VALUES (1, 'C001', 'Algorithms', 'Test Course')");
        }

        // insert book
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO books (id, book_id, title, author, library_id) VALUES (?,?, ?, ?, ?)")) {
            ps.setInt(1, 12);
            ps.setString(2, "B12");
            ps.setString(3, "Course Book");
            ps.setString(4, "A");
            ps.setInt(5, 1);
            ps.executeUpdate();
        }

        // mark book with course
        bookDAO.markBookWithCourse(12, 1);

        // verify the association exists
        try (PreparedStatement check = connection.prepareStatement(
            "SELECT * FROM course_books WHERE course_id = ? AND book_id = ?")) {
            check.setInt(1, 1);
            check.setInt(2, 12);
            try (ResultSet rs = check.executeQuery()) {
                assertTrue(rs.next(), "Association should be created");
            }
        }

        // unmark the association
        bookDAO.unmarkBookWithCourse(12, 1);

        // verify it's removed
        try (PreparedStatement check = connection.prepareStatement(
            "SELECT * FROM course_books WHERE course_id = ? AND book_id = ?")) {
            check.setInt(1, 1);
            check.setInt(2, 12);
            try (ResultSet rs = check.executeQuery()) {
                assertFalse(rs.next(), "Association should be removed");
            }
        }

        // cleanup
        bookDAO.delete(12);
    }

    @Test
    void testGetBooksForCourse_returnsList() throws SQLException {
        // create book and map to course
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO books (id, book_id, title, author, library_id) VALUES (?,?,?,?,?)")) {
            ps.setInt(1, 20);
            ps.setString(2, "B20");
            ps.setString(3, "CourseLinked");
            ps.setString(4, "X");
            ps.setInt(5, 1);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO course_books (course_id, book_id) VALUES (?, ?)")) {
            ps.setInt(1, 1);
            ps.setInt(2, 20);
            ps.executeUpdate();
        }

        var list = bookDAO.getBooksForCourse(1);
        assertNotNull(list);
        assertTrue(list.size() >= 1);

        // cleanup
        bookDAO.delete(20);
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM course_books WHERE course_id = ? AND book_id = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 20);
            ps.executeUpdate();
        }
    }

    @Test
    void testReadNonExistent_returnsNull() throws SQLException {
        assertNull(bookDAO.read(-9999));
    }
}
