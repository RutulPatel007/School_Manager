package com.schoolmanagement.dao;

import com.schoolmanagement.models.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends BaseDAO<Course> {

    public CourseDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Course course) throws SQLException {
        String query = "INSERT INTO courses (course_id, course_code, course_name, course_description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = createPreparedStatement(query, course.getCourseId(), course.getCourseCode(), course.getCourseName(), course.getCourseDescription())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Course read(int id) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        }
        return null;
    }


    public void update(String courseCode, String courseName, String courseDescription) throws SQLException {
        String query = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_code = ?";
        try (PreparedStatement ps = createPreparedStatement(query, courseName, courseDescription, courseCode)) {
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM courses WHERE course_id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id)) {
            ps.executeUpdate();
        }
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Course(
            rs.getInt("course_id"),
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getString("course_description")
        );
    }

    @Override
    public List<Course> mapResultSetToList(ResultSet rs) throws SQLException {
        rs = connection.createStatement().executeQuery("SELECT * FROM courses");
        List<Course> courses = new ArrayList<>();
        while (rs.next()) {
            courses.add(mapResultSetToEntity(rs));
        }
        return courses;
    }
}
