package com.schoolmanagement.tests;

import com.schoolmanagement.dao.StudentDAO;
import com.schoolmanagement.models.Book;
import com.schoolmanagement.models.Student;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {

    private static Connection connection;
    private static StudentDAO studentDAO;

    @BeforeAll
    static void setupAll() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        studentDAO = new StudentDAO(connection);

        // ensure a course exists
        try (Statement st = connection.createStatement()) {
            st.execute("INSERT IGNORE INTO courses (course_id, course_code, course_name, course_description) VALUES (1,'C001','Algorithms','Test')");
        }
    }

    @AfterAll
    static void tearAll() throws SQLException {
        if (connection != null) connection.close();
    }

    @BeforeEach
    void cleanBefore() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM enrollments");
            st.execute("DELETE FROM students");
            st.execute("DELETE FROM course_books");
            st.execute("DELETE FROM books");
        }
    }

    @Test
    void testCreateReadDeleteStudent() throws SQLException {
        Student s = new Student(401, "R401", "Alice", "2000-01-01", "Addr", 3.5f);
        studentDAO.create(s);

        Student read = studentDAO.read(401);
        assertNotNull(read);
        assertEquals("Alice", read.getName());

        studentDAO.delete(401);
        assertNull(studentDAO.read(401));
    }

    @Test
    void testUpdateAddressAndPersisted() throws SQLException {
        Student s = new Student(402, "R402", "Bob", "1999-05-05", "OldAddr", 3.0f);
        studentDAO.create(s);

        studentDAO.update("R402", "NewAddr");

        Student updated = studentDAO.read(402);
        assertNotNull(updated);
        assertEquals("NewAddr", updated.getAddress());

        studentDAO.delete(402);
    }

    @Test
    void testUpdateCGPA_changesValue() throws SQLException {
        Student s = new Student(403, "R403", "Charlie", "1998-07-07", "Addr3", 3.1f);
        studentDAO.create(s);

        studentDAO.updateCGPA(403, 4.0f);

        Student after = studentDAO.read(403);
        assertNotNull(after);
        assertEquals(4.0f, after.getCgpa(), 0.0001);

        studentDAO.delete(403);
    }

    @Test
    void testGetTopper_andEmptyCase() throws SQLException {
        // empty DB -> null
        List<Student> allBefore = studentDAO.mapResultSetToList(connection.createStatement().executeQuery("SELECT * FROM students"));
        for (Student s : allBefore) studentDAO.delete(s.getId());

        assertNull(studentDAO.getTopper());

        // create two students and ensure topper returned
        studentDAO.create(new Student(404, "R404", "Topper", "2001-01-01", "X", 3.9f));
        studentDAO.create(new Student(405, "R405", "Other", "2001-01-02", "Y", 3.2f));

        Student topper = studentDAO.getTopper();
        assertNotNull(topper);
        assertEquals("Topper", topper.getName());

        studentDAO.delete(404);
        studentDAO.delete(405);
    }

    @Test
    void testAddAndRemoveStudentFromCourse_affectsDB() throws SQLException {
        studentDAO.create(new Student(406, "R406", "EnrollMe", "2002-02-02", "Z", 3.0f));

        // ensure not enrolled
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?")) {
            ps.setInt(1, 406);
            ps.setInt(2, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertFalse(rs.next());
            }
        }

        studentDAO.addStudentToCourse(406, 1);

        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?")) {
            ps.setInt(1, 406);
            ps.setInt(2, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
            }
        }

        studentDAO.removeStudentFromCourse(406, 1);

        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?")) {
            ps.setInt(1, 406);
            ps.setInt(2, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertFalse(rs.next());
            }
        }

        studentDAO.delete(406);
    }

    @Test
    void testGetBooksForStudents_whenNoneAndWhenPresent() throws SQLException {
        // ensure student exists
        studentDAO.create(new Student(407, "R407", "Bookless", "2003-03-03", "Addr", 3.0f));

        // no books -> should return empty list (implementation returns empty or null; assert not null to kill null-return mutants)
        List<Book> none = studentDAO.getBooksforStudents(407);
        assertNotNull(none);
        assertEquals(0, none.size());

        // create a book, map to a course, enroll student -> then should return at least one
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO books (id, book_id, title, author, library_id) VALUES (?,?,?,?,?)")) {
            ps.setInt(1, 500);
            ps.setString(2, "B500");
            ps.setString(3, "StuBook");
            ps.setString(4, "Aut");
            ps.setInt(5, 1);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO course_books (course_id, book_id) VALUES (?,?)")) {
            ps.setInt(1, 1);
            ps.setInt(2, 500);
            ps.executeUpdate();
        }
        studentDAO.addStudentToCourse(407, 1);

        List<Book> some = studentDAO.getBooksforStudents(407);
        assertNotNull(some);
        assertTrue(some.size() >= 1);

        // cleanup
        studentDAO.delete(407);
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM course_books WHERE course_id = ? AND book_id = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 500);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM books WHERE id = ?")) {
            ps.setInt(1, 500);
            ps.executeUpdate();
        }
    }
}
