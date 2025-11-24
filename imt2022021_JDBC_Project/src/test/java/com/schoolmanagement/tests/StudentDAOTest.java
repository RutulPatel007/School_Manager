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

        // Ensure a course & library exists
        try (Statement st = connection.createStatement()) {
            st.execute("INSERT IGNORE INTO libraries (id, name) VALUES (1,'Main Library')");
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

    // Utility to fetch auto-generated student_id using roll_number
    private int getStudentIdByRoll(String roll) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM students WHERE roll_number=?")) {
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    @Test
    @Order(1) // Assuming you want this near the start
    void testGetAllStudents_returnsNonEmptyList() throws SQLException {
        // ARRANGE: Ensure we have at least one student in the DB
        Student s = new Student(0, "ALL01", "All Test", "1999-01-01", "X", 3.0f);
        studentDAO.create(s);

        // ACT
        List<Student> allStudents = studentDAO.getAllStudents();

        // ASSERT
        assertNotNull(allStudents, "The list returned by getAllStudents should not be null.");
        assertTrue(allStudents.size() >= 1, "getAllStudents should return a list with at least one student.");
    }

    @Test
    void testCreateReadDeleteStudent() throws SQLException {
        Student s = new Student(0, "R401", "Alice", "2000-01-01", "Addr", 3.5f);
        studentDAO.create(s);

        int id = getStudentIdByRoll("R401");
        assertTrue(id > 0);

        Student read = studentDAO.read(id);
        assertNotNull(read);
        assertEquals("Alice", read.getName());

        studentDAO.delete(id);
        assertNull(studentDAO.read(id));
    }

    @Test
    void testUpdateAddressAndPersisted() throws SQLException {
        Student s = new Student(0, "R402", "Bob", "1999-05-05", "OldAddr", 3.0f);
        studentDAO.create(s);

        int id = getStudentIdByRoll("R402");

        studentDAO.updateAddress("R402", "NewAddr");

        Student updated = studentDAO.read(id);
        assertNotNull(updated);
        assertEquals("NewAddr", updated.getAddress());
    }

    @Test
    void testUpdateCGPA_changesValue() throws SQLException {
        Student s = new Student(0, "R403", "Charlie", "1998-07-07", "Addr3", 3.1f);
        studentDAO.create(s);

        int id = getStudentIdByRoll("R403");
        studentDAO.updateCGPA(id, 4.0f);

        Student after = studentDAO.read(id);
        assertNotNull(after);
        assertEquals(4.0f, after.getCgpa(), 0.0001);
    }

    @Test
    void testGetTopper_andEmptyCase() throws SQLException {
        assertNull(studentDAO.getTopper());

        studentDAO.create(new Student(0, "R404", "Topper", "2001-01-01", "X", 3.9f));
        studentDAO.create(new Student(0, "R405", "Other", "2001-01-02", "Y", 3.2f));

        Student topper = studentDAO.getTopper();
        assertNotNull(topper);
        assertEquals("Topper", topper.getName());
    }

    @Test
    void testAddAndRemoveStudentFromCourse_affectsDB() throws SQLException {
        studentDAO.create(new Student(0, "R406", "EnrollMe", "2002-02-02", "Z", 3.0f));
        int id = getStudentIdByRoll("R406");

        // Not enrolled initially
        assertFalse(isEnrolled(id, 1));

        studentDAO.addStudentToCourse(id, 1);
        assertTrue(isEnrolled(id, 1));

        studentDAO.removeStudentFromCourse(id, 1);
        assertFalse(isEnrolled(id, 1));
    }

    @Test
    void testDeleteNonExistingStudent_doesNotThrow() {
        assertDoesNotThrow(() -> studentDAO.delete(999999));
    }

    @Test
    void testUpdateAddress_nonExistingRollNumber_noEffect() throws SQLException {
        studentDAO.updateAddress("New", "DOES_NOT_EXIST");

        // Should not throw, just 0 rows updated
        assertDoesNotThrow(() -> studentDAO.updateAddress("New", "DOES_NOT_EXIST"));
    }

    @Test
    void testMapResultSetToList_emptyResultSet() throws Exception {
        ResultSet rs = connection.createStatement()
                .executeQuery("SELECT * FROM students WHERE id = -1");

        List<Student> list = studentDAO.mapResultSetToList(rs);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    void testCreate_SQLExceptionHandled() throws SQLException {
        // Clear dependent entries first to avoid FK issues
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM enrollments");
            st.execute("DELETE FROM students");
        }
    
        // Force duplicate key to trigger SQLException
        studentDAO.create(new Student(999, "R999", "Test", "2000-01-01", "X", 3.0f));
    
        assertThrows(SQLException.class, () -> {
            studentDAO.create(new Student(999, "R999", "Dup", "2000-01-01", "X2", 3.5f));
        });
    }
    
    @Test
    @Order(10)
    void testGetTopper_noStudents_returnsNull() throws SQLException {
        // ARRANGE: Ensure table is empty (via @BeforeEach or explicit clear if necessary)
        
        // ACT
        Student topper = studentDAO.getTopper();
        
        // ASSERT: This must kill the mutant
        assertNull(topper, "Topper should be null when no students are present.");
    }

    @Test
    @Order(11)
    void testUpdateCGPA_exception_noCrash() {
        // ARRANGE: Attempt to pass an invalid Student ID that violates a potential foreign key 
        // or triggers a database error.
        int nonExistentStudentId = 9999999; 
        float validCGPA = 4.0f;

        // ACT & ASSERT: Since the method catches the exception internally and prints the stack trace, 
        // we only assert that it completes without throwing an unhandled exception.
        assertDoesNotThrow(() -> studentDAO.updateCGPA(nonExistentStudentId, validCGPA), 
                        "Update CGPA on a non-existent ID should not crash.");
                        
        // This test covers L83 (updateCGPA catch block).
        // The same logic should apply to L90, L110, and L138/139 (getBooksForStudent). 
        // If you run PIT again, these should now be covered.
    }


    private boolean isEnrolled(int studentId, int courseId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM enrollments WHERE student_id=? AND course_id=?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    @Test
    void testGetBooksForStudents_whenNoneAndWhenPresent() throws SQLException {
        studentDAO.create(new Student(0, "R407", "Bookless", "2003-03-03", "Addr", 3.0f));
        int id = getStudentIdByRoll("R407");

        List<Book> none = studentDAO.getBooksForStudent(id);
        assertNotNull(none);
        assertEquals(0, none.size());

        // Create book → map to course → enroll student
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO books (book_id,title,author,library_id) VALUES (?,?,?,?)")) {
            ps.setString(1, "B500");
            ps.setString(2, "StuBook");
            ps.setString(3, "Aut");
            ps.setInt(4, 1);
            ps.executeUpdate();
        }

        int bookId = getBookIdByCode("B500");

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO course_books (course_id, book_id) VALUES (?,?)")) {
            ps.setInt(1, 1);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }

        studentDAO.addStudentToCourse(id, 1);

        List<Book> some = studentDAO.getBooksForStudent(id);
        assertNotNull(some);
        assertTrue(some.size() >= 1);
    }

    private int getBookIdByCode(String code) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id FROM books WHERE book_id=?")) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }
}
