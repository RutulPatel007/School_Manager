package com.schoolmanagement.tests;

import com.schoolmanagement.dao.CourseDAO;
import com.schoolmanagement.models.Course;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest_Mutations {

    private static Connection connection;
    private static CourseDAO dao;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "admin");

        dao = new CourseDAO(connection);
    }

    @BeforeEach
    void clean() throws Exception {
        try (Statement s = connection.createStatement()) {
            s.execute("DELETE FROM course_books");
            s.execute("DELETE FROM courses");
        }
    }

    @Test
    void testUpdate_nonExistent_noChange() throws Exception {
        int affected = dao.update("NON", "X", "Y");
        assertEquals(0, affected);
    }

    @Test
    void testMapResultSetToList_empty() throws Exception {
        var rs = connection.createStatement().executeQuery("SELECT * FROM courses WHERE 1=0");
        assertEquals(0, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testMapResultSetToList_single() throws Exception {
        dao.create(new Course(800, "C800", "Test", "Description"));
        var rs = connection.createStatement().executeQuery("SELECT * FROM courses");
        assertEquals(1, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testRead_invalid_returnsNull() throws Exception {
        assertNull(dao.read(-888));
    }
}
