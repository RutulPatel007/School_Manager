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
        String query = "INSERT INTO books (id, book_id, title, author, library_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = createPreparedStatement(query, book.getId(), book.getBookId(), book.getTitle(), book.getAuthor(), book.getLibraryId())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Book read(int id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    public void update(String title, String Bookid, String author) throws SQLException {
        String query = "UPDATE books SET title = ?, author = ? WHERE  book_id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, title, author, Bookid)) {
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id)) {
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
        rs = connection.createStatement().executeQuery("SELECT * FROM books");
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            books.add(mapResultSetToEntity(rs));
        }
        return books;
    }

    public void markBookWithCourse(int bookId, int courseId) {
        String query = "INSERT INTO course_books (course_id, book_id) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void unmarkBookWithCourse(int bookId, int courseId) {
        String query = "DELETE FROM course_books WHERE course_id = ? AND book_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksForCourse(int courseId) {
        String query = "SELECT * FROM books WHERE id IN (SELECT book_id FROM course_books WHERE course_id = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    books.add(new Book(rs.getInt("id"), rs.getString("book_id"), rs.getString("title"), rs.getString("author"), rs.getInt("library_id")));
                }
                return books;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
