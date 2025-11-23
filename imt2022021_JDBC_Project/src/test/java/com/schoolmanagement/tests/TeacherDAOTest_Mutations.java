package com.schoolmanagement.tests;

import com.schoolmanagement.dao.TeacherDAO;
import com.schoolmanagement.models.Teacher;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherDAOTest_Mutations {

    private static Connection connection;
    private static TeacherDAO dao;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "admin");

        dao = new TeacherDAO(connection);
    }

    @BeforeEach
    void clean() throws Exception {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM teachers");
        }
    }

    @Test
    void testUpdateAddress_noSuchEmployee_noUpdate() throws Exception {
        int affected = dao.updateAddress("NewX", "INVALID");
        assertEquals(0, affected);
    }

    @Test
    void testIncrementSalary_invalidId_noChange() throws Exception {
        dao.incrementSalary(-999, 1000f);
        assertNull(dao.read(-999));
    }

    @Test
    void testMapResultSetToList_empty() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM teachers WHERE id = -1");
        assertEquals(0, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testMapResultSetToList_singleRow() throws Exception {
        dao.create(new Teacher(801, "E801", "One", "1990-01-01", "Addr", 1000f));
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM teachers");
        assertEquals(1, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testRead_invalidId_returnsNull() throws Exception {
        assertNull(dao.read(-333));
    }
}
