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
        // Establish the database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_db", "root", "admin");
        bookDAO = new BookDAO(connection);
    }

    @AfterAll
    static void closeDatabase() throws SQLException {
        // Close database connection
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    void setupTestData() throws SQLException {
        // Insert test data before each test
        String query = "INSERT INTO books (id, book_id, title, author, library_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.setString(2, "B101");
            ps.setString(3, "Introduction to Algorithms");
            ps.setString(4, "Thomas H. Cormen");
            ps.setInt(5, 1);  // Assuming library_id is 1
            ps.executeUpdate();
        }
    }

    @AfterEach
    void cleanupTestData() throws SQLException {
        // Clean up test data after each test
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.executeUpdate();
        }
    }

    @Test
    void testCreate() throws SQLException {
        Book book = new Book(2, "B102", "Data Structures", "Alfred V. Aho", 1);
        bookDAO.create(book);

        Book retrieved = bookDAO.read(2);
        assertNotNull(retrieved);
        assertEquals("Data Structures", retrieved.getTitle());
        assertEquals("Alfred V. Aho", retrieved.getAuthor());

        // Clean up
        bookDAO.delete(2);
    }

    @Test
    void testRead() throws SQLException {
        Book book = bookDAO.read(1);
        assertNotNull(book);
        assertEquals("Introduction to Algorithms", book.getTitle());
    }

    @Test
    void testUpdate() throws SQLException {
        Book book = bookDAO.read(1);
        assertNotNull(book);

        // Update book title
        book.setTitle("Algorithms - Revised Edition");
        bookDAO.update("Algorithms - Revised Edition", "B101", "Thomas H. Cormen");

        Book updatedBook = bookDAO.read(1);
        assertEquals("Algorithms - Revised Edition", updatedBook.getTitle());
    }

    @Test
    void testDelete() throws SQLException {
        // Insert a new book to delete
        String query = "INSERT INTO books (id, book_id, title, author, library_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 3);
            ps.setString(2, "B103");
            ps.setString(3, "Operating Systems Concepts");
            ps.setString(4, "Abraham Silberschatz");
            ps.setInt(5, 1);
            ps.executeUpdate();
        }

        bookDAO.delete(3);

        Book book = bookDAO.read(3);
        assertNull(book);
    }

    @Test
    void testMarkBookWithCourse() throws SQLException {
        bookDAO.markBookWithCourse(1, 1);  // Assuming course_id is 1

        // Verify the book is marked with the course
        String query = "SELECT * FROM course_books WHERE course_id = ? AND book_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "The book should be associated with the course.");
        }
    }

    @Test
    void testUnmarkBookWithCourse() throws SQLException {
        // Mark the book first
        bookDAO.markBookWithCourse(1, 1);

        // Unmark the book from the course
        bookDAO.unmarkBookWithCourse(1, 1);

        // Verify the book is no longer associated with the course
        String query = "SELECT * FROM course_books WHERE course_id = ? AND book_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();
            assertFalse(rs.next(), "The book should no longer be associated with the course.");
        }
    }

    @Test
    void testGetBooksForCourse() throws SQLException {
        // Mark a book with a course first
        bookDAO.markBookWithCourse(1, 1);

        List<Book> books = bookDAO.getBooksForCourse(1);
        assertNotNull(books);
        assertTrue(books.size() > 0, "There should be at least one book associated with the course.");
    }
}
