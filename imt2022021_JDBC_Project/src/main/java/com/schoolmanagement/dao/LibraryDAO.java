package com.schoolmanagement.dao;

import com.schoolmanagement.models.Book;
import com.schoolmanagement.models.Library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO extends BaseDAO<Library> {

    public LibraryDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Library library) throws SQLException {
        String query = "INSERT INTO libraries (id, name) VALUES (?, ?)";
        try (PreparedStatement ps = createPreparedStatement(query, library.getId(), library.getName())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Library read(int id) throws SQLException {
        String query = "SELECT * FROM libraries WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Fetch the library data
                Library library = new Library(rs.getInt("id"), rs.getString("name"));
                
                // Fetch the books for this library
                String booksQuery = "SELECT * FROM books WHERE library_id = ?";
                try (PreparedStatement psBooks = createPreparedStatement(booksQuery, id); ResultSet bookRs = psBooks.executeQuery()) {
                    while (bookRs.next()) {
                        Book book = new Book(
                            bookRs.getInt("id"),
                            bookRs.getString("book_id"),
                            bookRs.getString("title"),
                            bookRs.getString("author"),
                            bookRs.getInt("library_id")
                        );
                    }
                }
                return library;
            }
        }
        return null;
    }

    public void update(String library, int id) throws SQLException {
        String query = "UPDATE libraries SET name = ? WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, library, id)) {
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        // Delete books associated with the library first
        String deleteBooksQuery = "DELETE FROM books WHERE library_id = ?";
        try (PreparedStatement psBooks = createPreparedStatement(deleteBooksQuery, id)) {
            psBooks.executeUpdate();
        }

        // Now delete the library
        String deleteLibraryQuery = "DELETE FROM libraries WHERE id = ?";
        try (PreparedStatement psLibrary = createPreparedStatement(deleteLibraryQuery, id)) {
            psLibrary.executeUpdate();
        }
    }

    @Override
    protected Library mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Library(
            rs.getInt("id"),
            rs.getString("name")
        );
    }

    @Override
    public List<Library> mapResultSetToList(ResultSet rs) throws SQLException {
        String queString = "SELECT * FROM library";
        rs = connection.prepareStatement(queString).executeQuery();
        List<Library> libraries = new ArrayList<>();
        while (rs.next()) {
            libraries.add(mapResultSetToEntity(rs));
        }
        return libraries;
    }
}
