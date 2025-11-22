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
    static void setupAll() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        teacherDAO = new TeacherDAO(connection);
    }

    @AfterAll
    static void tearAll() throws SQLException {
        if (connection != null) connection.close();
    }

    @BeforeEach
    void cleanBefore() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM teachers");
        }
    }

    @Test
    void testCreateReadDeleteCycle() throws SQLException {
        Teacher t = new Teacher(601, "E601", "Teach", "1970-01-01", "Addr", 45000f);
        teacherDAO.create(t);

        Teacher read = teacherDAO.read(601);
        assertNotNull(read);
        assertEquals("Teach", read.getName());

        teacherDAO.delete(601);
        assertNull(teacherDAO.read(601));
    }

    @Test
    void testUpdateAddress_andPersisted() throws SQLException {
        teacherDAO.create(new Teacher(602, "E602", "Upd", "1971-02-02", "Old", 50000f));
        teacherDAO.update("NewAddr", "E602");

        Teacher after = teacherDAO.read(602);
        assertNotNull(after);
        assertEquals("NewAddr", after.getAddress());

        teacherDAO.delete(602);
    }

    @Test
    void testIncrementSalary_mutatesValue() throws SQLException {
        teacherDAO.create(new Teacher(603, "E603", "Sal", "1972-03-03", "Addr", 50000f));

        // read before
        Teacher before = teacherDAO.read(603);
        assertNotNull(before);
        float old = before.getSalary();

        teacherDAO.incrementSalary(603, 2000f);

        Teacher after = teacherDAO.read(603);
        assertNotNull(after);
        assertEquals(old + 2000f, after.getSalary(), 0.001);

        teacherDAO.delete(603);
    }

    @Test
    void testGetHighestPaidTeacher_andEmptyCase() throws SQLException {
        // empty -> null
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM teachers");
        }
        assertNull(teacherDAO.getHighestPaidTeacher());

        // create two teachers
        teacherDAO.create(new Teacher(604, "E604", "High", "1973-04-04", "A", 70000f));
        teacherDAO.create(new Teacher(605, "E605", "Low", "1974-05-05", "B", 40000f));

        Teacher highest = teacherDAO.getHighestPaidTeacher();
        assertNotNull(highest);
        assertEquals("High", highest.getName());

        teacherDAO.delete(604);
        teacherDAO.delete(605);
    }

    @Test
    void testMapResultSetToList_emptyAndWithData() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM teachers");
        }

        List<Teacher> empty = teacherDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM teachers"));
        assertNotNull(empty);
        assertEquals(0, empty.size());

        // insert one
        teacherDAO.create(new Teacher(606, "E606", "One", "1975-06-06", "C", 48000f));
        List<Teacher> some = teacherDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM teachers"));
        assertNotNull(some);
        assertTrue(some.size() >= 1);

        teacherDAO.delete(606);
    }
}
