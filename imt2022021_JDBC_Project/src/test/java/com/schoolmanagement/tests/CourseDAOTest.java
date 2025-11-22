package com.schoolmanagement.tests;

import com.schoolmanagement.dao.CourseDAO;
import com.schoolmanagement.models.Course;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest {

    private static Connection connection;
    private static CourseDAO courseDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Establish the database connection
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        
        courseDAO = new CourseDAO(connection);
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
        String query = "INSERT INTO courses (course_id, course_code, course_name, course_description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 101);
            ps.setString(2, "CS101");
            ps.setString(3, "Introduction to Computer Science");
            ps.setString(4, "An introductory course on computer science.");
            ps.executeUpdate();
        }
    }

    @AfterEach
    void cleanupTestData() throws SQLException {
        // Clean up test data after each test
        String query = "DELETE FROM courses WHERE course_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 101);
            ps.executeUpdate();
        }
    }

    @Test
    void testCreate() throws SQLException {
        Course course = new Course(102, "CS102", "Data Structures", "An advanced course on data structures.");
        courseDAO.create(course);

        Course retrieved = courseDAO.read(102);
        assertNotNull(retrieved);
        assertEquals("CS102", retrieved.getCourseCode());
        assertEquals("Data Structures", retrieved.getCourseName());
        assertEquals("An advanced course on data structures.", retrieved.getCourseDescription());

        // Clean up
        courseDAO.delete(102);
    }

    @Test
    void testRead() throws SQLException {
        Course course = courseDAO.read(101);
        assertNotNull(course);
        assertEquals("CS101", course.getCourseCode());
        assertEquals("Introduction to Computer Science", course.getCourseName());
    }

    @Test
    void testUpdate() throws SQLException {
        Course course = courseDAO.read(101);
        assertNotNull(course);

        // Update course description
        course.setCourseDescription("A comprehensive introduction to computer science.");
        courseDAO.update(course.getCourseCode(), course.getCourseName(), course.getCourseDescription());

        Course updatedCourse = courseDAO.read(101);
        assertEquals("A comprehensive introduction to computer science.", updatedCourse.getCourseDescription());
    }

    @Test
    void testDelete() throws SQLException {
        // Insert a new course to delete
        String query = "INSERT INTO courses (course_id, course_code, course_name, course_description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 103);
            ps.setString(2, "CS103");
            ps.setString(3, "Algorithms");
            ps.setString(4, "A course on algorithms.");
            ps.executeUpdate();
        }

        courseDAO.delete(103);

        Course course = courseDAO.read(103);
        assertNull(course);
    }

    @Test
    void testMapResultSetToList() throws SQLException {
        List<Course> courses = courseDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM courses"));
        assertNotNull(courses);
        assertTrue(courses.size() > 0, "There should be at least one course in the database.");
    }
}
