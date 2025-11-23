package com.schoolmanagement.dao;

import com.schoolmanagement.models.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO extends BaseDAO<Teacher> {

    public TeacherDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Teacher teacher) throws SQLException {
        String query = "INSERT INTO teachers (emp_id, name, dob, address, salary) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = createPreparedStatement(query,
                teacher.getEmpId(),
                teacher.getName(),
                teacher.getDob(),
                teacher.getAddress(),
                teacher.getSalary())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Teacher read(int id) throws SQLException {
        String query = "SELECT * FROM teachers WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapResultSetToEntity(rs);
        }
        return null;
    }

    

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM teachers WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id)) {
            ps.executeUpdate();
        }
    }

    @Override
    protected Teacher mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Teacher(
                rs.getInt("id"),
                rs.getString("emp_id"),
                rs.getString("name"),
                rs.getString("dob"),
                rs.getString("address"),
                rs.getFloat("salary")
        );
    }

    @Override
    public List<Teacher> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        while (rs.next()) teachers.add(mapResultSetToEntity(rs));
        return teachers;
    }

    

    public List<Teacher> getAllTeachers() throws SQLException {
        return executeQueryForList("SELECT * FROM teachers");
    }

    public int updateAddress(String newAddress, String empId) throws SQLException {
        String query = "UPDATE teachers SET address = ? WHERE emp_id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, newAddress, empId)) {
            return ps.executeUpdate();
        }
    }
    

    public void incrementSalary(int id, float inc) {
        try (PreparedStatement ps =
                     connection.prepareStatement("UPDATE teachers SET salary = salary + ? WHERE id = ?")) {
            ps.setFloat(1, inc);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Teacher getHighestPaidTeacher() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM teachers ORDER BY salary DESC LIMIT 1")) {
            if (rs.next()) return mapResultSetToEntity(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
