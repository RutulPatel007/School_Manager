package com.schoolmanagement.tests;

import com.schoolmanagement.dao.TeacherDAO;
import com.schoolmanagement.models.Teacher;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherDAOTest {

    private static Connection connection;
    private static TeacherDAO teacherDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Establish the database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_db", "root", "admin");
        teacherDAO = new TeacherDAO(connection);
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
        String query = "INSERT INTO teachers (id, emp_id, name, dob, address, salary) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 2);
            ps.setString(2, "T1234");
            ps.setString(3, "John Doe");
            ps.setString(4, "1985-03-12");
            ps.setString(5, "789 Oak Street");
            ps.setFloat(6, 50000f);
            ps.executeUpdate();
        }
    }

    @AfterEach
    void cleanupTestData() throws SQLException {
        // Clean up test data after each test
        String query = "DELETE FROM teachers WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 2);
            ps.executeUpdate();
        }
    }

    @Test
    void testCreate() throws SQLException {
        Teacher teacher = new Teacher(3, "T5678", "Jane Smith", "1990-07-25", "123 Pine Street", 60000f);
        teacherDAO.create(teacher);

        Teacher retrieved = teacherDAO.read(3);
        assertNotNull(retrieved);
        assertEquals("Jane Smith", retrieved.getName());
        assertEquals(60000f, retrieved.getSalary(), 0.01f);

        // Clean up
        teacherDAO.delete(3);
    }

    @Test
    void testRead() throws SQLException {
        Teacher teacher = teacherDAO.read(2);
        assertNotNull(teacher);
        assertEquals("John Doe", teacher.getName());
    }

    @Test
    void testUpdate() throws SQLException {
        Teacher teacher = teacherDAO.read(2);
        assertNotNull(teacher);

        teacher.setAddress("Brompton 107");;
        teacherDAO.update(teacher.getAddress(), teacher.getEmpId());

        Teacher updatedTeacher = teacherDAO.read(2);
        assertEquals("Brompton 107", updatedTeacher.getAddress(),"Address should be updated to Brompton 107");
    }

    @Test
    void testDelete() throws SQLException {
        teacherDAO.delete(2);

        Teacher teacher = teacherDAO.read(2);
        assertNull(teacher);
    }

    @Test
    void testIncrementSalary() throws SQLException {
        teacherDAO.incrementSalary(2, 1000f);

        Teacher teacher = teacherDAO.read(2);
        assertEquals(51000f, teacher.getSalary(), 0.01f);
    }

    @Test
    void testGetHighestPaidTeacher() throws SQLException {
        Teacher highestPaid = teacherDAO.getHighestPaidTeacher();
        assertNotNull(highestPaid);
        assertTrue(highestPaid.getSalary() >= 50000f); // Assumes the test data has a teacher with salary >= 50000
    }

    @Test
    void testMapResultSetToList() throws SQLException {
        List<Teacher> teachers = teacherDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM teachers"));
        assertNotNull(teachers);
        assertTrue(teachers.size() > 0, "There should be at least one teacher in the database.");
    }
}
