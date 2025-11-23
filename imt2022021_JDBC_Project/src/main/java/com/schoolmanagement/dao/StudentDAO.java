package com.schoolmanagement.dao;

import com.schoolmanagement.models.Book;
import com.schoolmanagement.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends BaseDAO<Student> {

    public StudentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Student student) throws SQLException {
        String query = "INSERT INTO students (roll_number, name, dob, address, cgpa) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = createPreparedStatement(query,
                student.getRollNumber(),
                student.getName(),
                student.getDob(),
                student.getAddress(),
                student.getCgpa())) {
            ps.executeUpdate();
        }
    }

    @Override
    public Student read(int id) throws SQLException {
        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapResultSetToEntity(rs);
        }
        return null;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement ps = createPreparedStatement(query, id)) {
            ps.executeUpdate();
        }
    }

    @Override
    protected Student mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("id"),
            rs.getString("roll_number"),
            rs.getString("name"),
            rs.getString("dob"),
            rs.getString("address"),
            rs.getFloat("cgpa")
        );
    }

    @Override
    public List<Student> mapResultSetToList(ResultSet rs) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (rs.next()) students.add(mapResultSetToEntity(rs));
        return students;
    }

    public List<Student> getAllStudents() throws SQLException {
        return executeQueryForList("SELECT * FROM students");
    }

    public int updateAddress(String rollNumber, String address) throws SQLException {
        String query = "UPDATE students SET address = ? WHERE roll_number = ?";
        try (PreparedStatement ps = createPreparedStatement(query, address, rollNumber)) {
            return ps.executeUpdate();
        }
    }

    public void updateCGPA(int studentId, float newCGPA) {
        String query = "UPDATE students SET cgpa = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setFloat(1, newCGPA);
            ps.setInt(2, studentId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Student getTopper() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY cgpa DESC LIMIT 1")) {
            if (rs.next()) return mapResultSetToEntity(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void addStudentToCourse(int studentId, int courseId) throws SQLException {
        String query = "INSERT INTO enrollments (course_id, student_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            pstmt.setInt(2, studentId);
            pstmt.executeUpdate();
        }
    }
    

    public void removeStudentFromCourse(int studentId, int courseId) {
        String q = "DELETE FROM enrollments WHERE course_id = ? AND student_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Book> getBooksForStudent(int studentId) {
        String q = """
            SELECT * FROM books 
            WHERE id IN (
                SELECT book_id FROM course_books 
                WHERE course_id IN (
                    SELECT course_id FROM enrollments WHERE student_id = ?
                )
            )
        """;
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new Book(
                    rs.getInt("id"),
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("library_id")
                ));
            }
            return books;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
