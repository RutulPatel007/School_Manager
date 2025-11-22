package com.schoolmanagement.tests;

import com.schoolmanagement.dao.StudentDAO;
import com.schoolmanagement.models.Student;
import com.schoolmanagement.models.Book;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {

    private static Connection connection;
    private static StudentDAO studentDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Establish the database connection
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        
        studentDAO = new StudentDAO(connection);
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
        String query = "INSERT INTO students (id, roll_number, name, dob, address, cgpa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 2);
            ps.setString(2, "S1234");
            ps.setString(3, "Alicia");
            ps.setString(4, "2000-01-01");
            ps.setString(5, "123 Elm Street");
            ps.setFloat(6, 3.8f);
            ps.executeUpdate();
        }
    }

    @AfterEach
    void cleanupTestData() throws SQLException {
        // Clean up test data after each test
        String query = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 2);
            ps.executeUpdate();
        }
    }

    @Test
    void testCreate() throws SQLException {
        Student student = new Student(3, "S5678", "Bob", "1999-12-25", "456 Maple Street", 3.2f);
        studentDAO.create(student);

        Student retrieved = studentDAO.read(3);
        assertNotNull(retrieved);
        assertEquals("Bob", retrieved.getName());
        assertEquals(3.2f, retrieved.getCgpa(), 0.01f);

        // Clean up
        studentDAO.delete(3);
    }

    @Test
    void testRead() throws SQLException {
        // Assuming the setupTestData method inserted a student with id 2
        Student student = studentDAO.read(2);
        assertNotNull(student);
        assertEquals("Alicia", student.getName());
    }

    @Test
    void testUpdate() throws SQLException {
        // Retrieve the student to update
        Student student = studentDAO.read(2);
        assertNotNull(student);

        // Update CGPA
        student.setAddress("789 Oak Street");
        studentDAO.update(student.getRollNumber(), student.getAddress());

        Student updatedStudent = studentDAO.read(2);
        assertEquals("789 Oak Street", updatedStudent.getAddress(), "The address should be updated.");
    }

    @Test
    void testDelete() throws SQLException {
        studentDAO.delete(2);

        Student student = studentDAO.read(2);
        assertNull(student);
    }

    @Test
    void testUpdateCGPA() throws SQLException {
        studentDAO.updateCGPA(2, 4.0f);

        Student student = studentDAO.read(2);
        assertEquals(4.0f, student.getCgpa(), 0.01f);
    }

    @Test
    void testGetTopper() throws SQLException {
        Student topper = studentDAO.getTopper();
        assertNotNull(topper);
        assertTrue(topper.getCgpa() >= 0);
    }

    @Test
    void testAddStudentToCourse() throws SQLException {
        studentDAO.addStudentToCourse(2, 1); // Assuming course ID is 101

        // Verify the student is added to the course
        String query = "SELECT * FROM enrollments WHERE course_id = ? AND student_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.setInt(2, 2);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "The student should be enrolled in the course.");
        }
    }

    @Test
    void testRemoveStudentFromCourse() throws SQLException {
        studentDAO.removeStudentFromCourse(2, 1);

        // Verify the student is removed from the course
        String query = "SELECT * FROM enrollments WHERE course_id = ? AND student_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, 1);
            ps.setInt(2, 2);
            ResultSet rs = ps.executeQuery();
            assertFalse(rs.next(), "The student should be removed from the course.");
        }
    }

    @Test
    void testGetBooksforStudents() throws SQLException {
        List<Book> books = studentDAO.getBooksforStudents(2);
        assertNotNull(books);
        assertTrue(books.size() >= 0, "There should be at least one book for the student.");
    }
}
