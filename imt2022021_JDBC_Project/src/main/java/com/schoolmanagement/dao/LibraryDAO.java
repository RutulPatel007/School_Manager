package com.schoolmanagement.dao;

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
        String q = "INSERT INTO libraries (name) VALUES (?)";
        try (PreparedStatement ps = createPreparedStatement(q, library.getName())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Library read(int id) throws SQLException {
        String q = "SELECT * FROM libraries WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(q, id);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapResultSetToEntity(rs);
        }
        return null;
    }

    @Override
    public void delete(int id) throws SQLException {
        String q = "DELETE FROM libraries WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(q, id)) {
            ps.executeUpdate();
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
        List<Library> libraries = new ArrayList<>();
        while (rs.next()) libraries.add(mapResultSetToEntity(rs));
        return libraries;
    }

    public List<Library> getAllLibraries() throws SQLException {
        return executeQueryForList("SELECT * FROM libraries");
    }

    public void update(int id, String name) throws SQLException {
        String q = "UPDATE libraries SET name = ? WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(q, name, id)) {
            ps.executeUpdate();
        }
    }
}
