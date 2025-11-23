package com.schoolmanagement.tests;

import com.schoolmanagement.dao.StudentDAO;
import com.schoolmanagement.models.Student;
import com.schoolmanagement.models.Book;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest_Mutations {

    private static Connection connection;
    private static StudentDAO dao;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "admin");

        dao = new StudentDAO(connection);

        try (Statement s = connection.createStatement()) {
            s.execute("INSERT IGNORE INTO courses (course_id, course_code, course_name, course_description) VALUES (2,'C002','X','Y')");
            s.execute("INSERT IGNORE INTO libraries (id, name) VALUES (1,'L1')");
        }
    }

    @BeforeEach
    void clean() throws Exception {
        try (Statement s = connection.createStatement()) {
            s.execute("DELETE FROM enrollments");
            s.execute("DELETE FROM students");
            s.execute("DELETE FROM course_books");
            s.execute("DELETE FROM books");
        }
    }

    @Test
    void testUpdateAddress_invalidRollNo_noChange() throws Exception {
        int affected = dao.updateAddress("RX", "NOPE");
        assertEquals(0, affected);
    }

    @Test
    void testUpdateCGPA_invalidId_doesNotAffectAnyRow() throws Exception {
        int beforeCount = countStudents();

        dao.updateCGPA(-999, 4.0f);

        int afterCount = countStudents();
        assertEquals(beforeCount, afterCount);
    }

    private int countStudents() throws Exception {
        try (Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM students")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    

    @Test
    void testAddStudentToCourse_invalidCourse_throws() throws Exception {
        dao.create(new Student(900, "R900", "X", "2000-01-01", "Addr", 3.0f));

        assertThrows(SQLException.class, () -> {
            dao.addStudentToCourse(900, -55);  // invalid course
        });
    }

    @Test
    void testRemoveStudentFromCourse_invalid_noCrash() {
        assertDoesNotThrow(() -> dao.removeStudentFromCourse(-99, -88));
    }

    @Test
    void testGetBooksForStudent_empty() throws Exception {
        dao.create(new Student(901, "R901", "Y", "2000-01-01", "Addr", 3.0f));
        List<Book> books = dao.getBooksForStudent(901);
        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    void testMapResultSetToList_empty() throws Exception {
        var rs = connection.createStatement().executeQuery("SELECT * FROM students WHERE id = -1");
        assertEquals(0, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testMapResultSetToList_singleRow() throws Exception {
        dao.create(new Student(902, "R902", "Z", "2000-01-01", "Addr", 3.0f));
        var rs = connection.createStatement().executeQuery("SELECT * FROM students");
        assertEquals(1, dao.mapResultSetToList(rs).size());
    }

    @Test
    void testRead_invalidId_returnsNull() throws Exception {
        assertNull(dao.read(-777));
    }
}
