package com.schoolmanagement.tests;

import com.schoolmanagement.dao.BookDAO;
import com.schoolmanagement.models.Book;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest_Mutations {

    private static Connection connection;
    private static BookDAO dao;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "admin");

        dao = new BookDAO(connection);

        try (Statement s = connection.createStatement()) {
            s.execute("INSERT IGNORE INTO libraries (id,name) VALUES (1,'L1')");
            s.execute("INSERT IGNORE INTO courses (course_id, course_code, course_name, course_description) VALUES (2,'C002','XX','YY')");
        }
    }

    @BeforeEach
    void clean() throws Exception {
        try (Statement s = connection.createStatement()) {
            s.execute("DELETE FROM course_books");
            s.execute("DELETE FROM books");
        }
    }

    @Test
    void testUpdate_nonExistentBook_noChange() throws Exception {
        int affected = dao.update("New", "B_NO", "A");
        assertEquals(0, affected);
    }

    @Test
    void testMarkBookWithCourse_invalidBook() {
        assertDoesNotThrow(() -> dao.markBookWithCourse(-99, 2));
    }

    @Test
    void testUnmarkBookWithCourse_invalid() {
        assertDoesNotThrow(() -> dao.unmarkBookWithCourse(-99, -88));
    }

    @Test
    void testGetBooksForCourse_empty() throws Exception {
        List<Book> list = dao.getBooksForCourse(2);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    void testMapResultSetToList_empty() throws Exception {
        var rs = connection.createStatement().executeQuery("SELECT * FROM books WHERE id = -1");
        assertEquals(0, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testMapResultSetToList_single() throws Exception {
        dao.create(new Book(900, "B900", "Title", "Auth", 1));
        var rs = connection.createStatement().executeQuery("SELECT * FROM books");
        assertEquals(1, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testRead_invalid_returnsNull() throws Exception {
        assertNull(dao.read(-555));
    }
}
