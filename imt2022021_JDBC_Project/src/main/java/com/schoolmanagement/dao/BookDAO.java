package com.schoolmanagement.dao;

import com.schoolmanagement.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO extends BaseDAO<Book> {

    public BookDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Book book) throws SQLException {
        String q = "INSERT INTO books (book_id, title, author, library_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = createPreparedStatement(q,
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getLibraryId())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Book read(int id) throws SQLException {
        String q = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(q, id);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapResultSetToEntity(rs);
        }
        return null;
    }

    @Override
    public void delete(int id) throws SQLException {
        String q = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(q, id)) {
            ps.executeUpdate();
        }
    }

    @Override
    protected Book mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("library_id")
        );
    }

    @Override
    public List<Book> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (rs.next()) books.add(mapResultSetToEntity(rs));
        return books;
    }

    public List<Book> getAllBooks() throws SQLException {
        return executeQueryForList("SELECT * FROM books");
    }

    public int update(String bookId, String title, String author) throws SQLException {
        String q = "UPDATE books SET title = ?, author = ? WHERE book_id = ?";
        try (PreparedStatement ps = createPreparedStatement(q, title, author, bookId)) {
            return ps.executeUpdate();
        }
    }

    public void markBookWithCourse(int bookId, int courseId) {
        String q = "INSERT INTO course_books (course_id, book_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, courseId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void unmarkBookWithCourse(int bookId, int courseId) {
        String q = "DELETE FROM course_books WHERE course_id = ? AND book_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, courseId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Book> getBooksForCourse(int courseId) {
        String q = "SELECT * FROM books WHERE id IN (SELECT book_id FROM course_books WHERE course_id = ?)";
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            List<Book> books = new ArrayList<>();
            while (rs.next()) books.add(mapResultSetToEntity(rs));
            return books;

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
