package com.schoolmanagement.dao;

import java.sql.*;
import java.util.List;

public abstract class BaseDAO<T> {
    protected Connection connection;

    // Constructor to initialize the database connection
    public BaseDAO(Connection connection) {
        this.connection = connection;
    }

    // Abstract methods for CRUD operations
    public abstract void create(T obj) throws SQLException;

    public abstract T read(int id) throws SQLException;

    public abstract void delete(int id) throws SQLException;

    // Helper method to close PreparedStatement and ResultSet resources
    // protected void closeResources(PreparedStatement ps, ResultSet rs) {
    //     try {
    //         if (rs != null) {
    //             rs.close();
    //         }
    //         if (ps != null) {
    //             ps.close();
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    // Utility method to create a PreparedStatement with a query and parameters
    protected PreparedStatement createPreparedStatement(String query, Object... params) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }

    // A method to map the ResultSet to a specific entity
    // Subclasses must override this method to map the ResultSet to their entity type
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    // A method to map the ResultSet to a list of entities (useful for fetching lists of records)
    // Subclasses must override this method to return a list of entities
    public abstract List<T> mapResultSetToList(ResultSet rs) throws SQLException;

    // A utility method to execute a query and return a list of entities (e.g., fetching all rows of a table)
    protected List<T> executeQueryForList(String query, Object... params) throws SQLException {
        try (PreparedStatement ps = createPreparedStatement(query, params);
             ResultSet rs = ps.executeQuery()) {
            return mapResultSetToList(rs);  // Override this in subclasses
        }
    }
}
