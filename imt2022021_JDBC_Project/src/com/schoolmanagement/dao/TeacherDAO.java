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
        String query = "INSERT INTO teachers (id, emp_id, name, dob, address, salary) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = createPreparedStatement(query, teacher.getId(), teacher.getEmpId(), teacher.getName(), teacher.getDob(), teacher.getAddress(), teacher.getSalary())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Teacher read(int id) throws SQLException {
        String query = "SELECT * FROM teachers WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    public void update(String ad, String emp) throws SQLException {
        String query = "UPDATE teachers SET address = ? WHERE emp_id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, ad, emp)) {
            ps.executeUpdate();
        }
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
        rs = connection.createStatement().executeQuery("SELECT * FROM teachers");
        List<Teacher> teachers = new ArrayList<>();
        while (rs.next()) {
            teachers.add(mapResultSetToEntity(rs));
        }
        return teachers;
    }

    public void incrementSalary(int teacherId, float incrementAmount) {
        String query = "UPDATE teachers SET salary = salary + ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setFloat(1, incrementAmount);
            pstmt.setInt(2, teacherId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Teacher getHighestPaidTeacher() {
        String query = "SELECT * FROM teachers ORDER BY salary DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return new Teacher(rs.getInt("id"), rs.getString("emp_id"),
                    rs.getString("name"), rs.getString("dob"), rs.getString("address"),
                    rs.getFloat("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
