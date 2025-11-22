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
    static void setup() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        courseDAO = new CourseDAO(connection);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null) connection.close();
    }

    @BeforeEach
    void clean() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM course_books");
            st.execute("DELETE FROM courses");
        }
    }

    @Test
    void testCreateReadDeleteCycle() throws SQLException {
        Course c = new Course(201, "CS201", "OS", "Operating Systems");
        courseDAO.create(c);

        Course read = courseDAO.read(201);
        assertNotNull(read);
        assertEquals("CS201", read.getCourseCode());

        courseDAO.delete(201);
        assertNull(courseDAO.read(201));
    }

    @Test
    void testUpdatePersistsChange() throws SQLException {
        Course c = new Course(202, "CS202", "DS", "Data Structures");
        courseDAO.create(c);

        // update description
        courseDAO.update("CS202", "DS Advanced", "Updated Description");

        Course updated = courseDAO.read(202);
        assertNotNull(updated);
        assertEquals("Updated Description", updated.getCourseDescription());

        courseDAO.delete(202);
    }

    @Test
    void testMapResultSetToList_withAndWithoutData() throws SQLException {
        // empty state
        List<Course> empty = courseDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM courses"));
        assertNotNull(empty);
        assertEquals(0, empty.size());

        // insert and test non-empty
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO courses (course_id, course_code, course_name, course_description) VALUES (?,?,?,?)")) {
            ps.setInt(1, 301);
            ps.setString(2, "CS301");
            ps.setString(3, "Algo");
            ps.setString(4, "Algorithms");
            ps.executeUpdate();
        }

        List<Course> nonEmpty = courseDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM courses"));
        assertNotNull(nonEmpty);
        assertTrue(nonEmpty.size() >= 1);

        // cleanup
        courseDAO.delete(301);
    }

    @Test
    void testReadNonExistentReturnsNull() throws SQLException {
        assertNull(courseDAO.read(-1));
    }
}
