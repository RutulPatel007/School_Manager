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
        // DAO create() uses auto-increment, so courseId from constructor is ignored.
        Course c = new Course(0, "CS201", "OS", "Operating Systems");
        courseDAO.create(c);

        int id = fetchCourseId("CS201");
        assertTrue(id > 0);

        Course read = courseDAO.read(id);
        assertNotNull(read);
        assertEquals("CS201", read.getCourseCode());

        courseDAO.delete(id);
        assertNull(courseDAO.read(id));
    }

    @Test
    void testUpdatePersistsChange() throws SQLException {
        Course c = new Course(0, "CS202", "DS", "Data Structures");
        courseDAO.create(c);

        int id = fetchCourseId("CS202");
        assertTrue(id > 0);

        courseDAO.update("CS202", "DS Advanced", "Updated Description");

        Course updated = courseDAO.read(id);
        assertNotNull(updated);
        assertEquals("Updated Description", updated.getCourseDescription());
    }

    @Test
    void testMapResultSetToList_withAndWithoutData() throws SQLException {
        // empty
        List<Course> empty = courseDAO.getAllCourses();
        assertNotNull(empty);
        assertEquals(0, empty.size());

        // insert 1 row
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO courses (course_code, course_name, course_description) VALUES (?,?,?)"
        )) {
            ps.setString(1, "CS301");
            ps.setString(2, "Algo");
            ps.setString(3, "Algorithms");
            ps.executeUpdate();
        }

        List<Course> list = courseDAO.getAllCourses();
        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }

    @Test
    void testReadNonExistentReturnsNull() throws SQLException {
        assertNull(courseDAO.read(-1));
    }

    @Test
    void testDeleteNonExistingCourse_doesNotThrow() {
        assertDoesNotThrow(() -> courseDAO.delete(999999));
    }

    @Test
    void testUpdateNonExistingCourse_noThrow() {
        assertDoesNotThrow(() -> courseDAO.update("NO_SUCH", "X", "Y"));
    }

    @Test
    void testMapResultSetToList_emptyCase() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM courses WHERE course_id = -1");
        List<Course> list = courseDAO.mapResultSetToList(rs);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    



    // Utility method: fetch course_id from course_code
    private int fetchCourseId(String code) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT course_id FROM courses WHERE course_code = ?"
        )) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("course_id");
        }
        return -1;
    }
}
