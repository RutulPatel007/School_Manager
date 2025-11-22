package com.schoolmanagement.services;

import com.schoolmanagement.dao.*;
import com.schoolmanagement.models.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SchoolService {

    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;
    private final BookDAO bookDAO;
    private final LibraryDAO libraryDAO;
    private ResultSet rs;

    public SchoolService(Connection connection) {
        this.studentDAO = new StudentDAO(connection);
        this.teacherDAO = new TeacherDAO(connection);
        this.courseDAO = new CourseDAO(connection);
        this.bookDAO = new BookDAO(connection);
        this.libraryDAO = new LibraryDAO(connection);
    }

    // Student Management Methods
    public void addStudent(Student student) throws SQLException {
        studentDAO.create(student);
        System.out.println("Student added: " + student.getName());
    }

    public void getStudentById(int id) throws SQLException {
        Student st = studentDAO.read(id);
        System.out.println(st);
    }

    public void getAllStudents() throws SQLException {
        List<Student> st = studentDAO.mapResultSetToList(rs);
        st.forEach(student -> System.out.println(student));
    }

    public void updateStudent(String rn, String address) throws SQLException {
        studentDAO.update(rn, address);
        System.out.println("Student Address Updated");
    }

    public void deleteStudent(int id) throws SQLException {
        studentDAO.delete(id);
        System.out.println("Student deleted with ID: " + id);
    }

    // Teacher Management Methods
    public void addTeacher(Teacher teacher) throws SQLException {
        teacherDAO.create(teacher);
        System.out.println("Teacher added: " + teacher.getName());
    }

    public void getTeacherById(int id) throws SQLException {
        Teacher th = teacherDAO.read(id);
        System.out.println(th);
    }

    public void getAllTeachers() throws SQLException {
        List<Teacher> t =  teacherDAO.mapResultSetToList(rs);
        t.forEach(teacher -> System.out.println(teacher));
    }

    public void updateTeacher(String add, String emp) throws SQLException {
        teacherDAO.update(add, emp);
        System.out.println("Teacher updated");
    }

    public void deleteTeacher(int id) throws SQLException {
        teacherDAO.delete(id);
        System.out.println("Teacher deleted with ID: " + id);
    }

    // Course Management Methods
    public void addCourse(Course course) throws SQLException {
        courseDAO.create(course);
        System.out.println("Course added: " + course.getCourseName());
    }

    public void getCourseById(int id) throws SQLException {
        Course cs = courseDAO.read(id);
        System.out.println(cs);
    }

    public void getAllCourses() throws SQLException {
        List<Course> cs = courseDAO.mapResultSetToList(rs);
        cs.forEach(course -> System.out.println(course));
    }

    public void updateCourse(String code, String name, String desc) throws SQLException {
        courseDAO.update(code, name, desc);
        System.out.println("Course updated");
    }

    public void deleteCourse(int id) throws SQLException {
        courseDAO.delete(id);
        System.out.println("Course deleted with ID: " + id);
    }

    // Library and Book Management Methods
    public void addBook(Book book) throws SQLException {
        bookDAO.create(book);
        System.out.println(book);
    }

    public void getBookById(int id) throws SQLException {
        Book b = bookDAO.read(id);
        System.out.println(b);
    }

    public void getAllBooks() throws SQLException {
        List<Book>  b= bookDAO.mapResultSetToList(rs);
        b.forEach(book -> System.out.println(book));
    }

    public void updateBook(String title, String author, String id) throws SQLException {
        bookDAO.update(title, id, author);
        System.out.println("Book updated: " + title);
    }

    public void deleteBook(int id) throws SQLException {
        bookDAO.delete(id);
        System.out.println("Book deleted with ID: " + id);
    }

    public void addLibrary(Library library) throws SQLException {
        libraryDAO.create(library);
        System.out.println(library);
    }

    public void getLibraryById(int id) throws SQLException {
        Library lb = libraryDAO.read(id);
        System.out.println(lb);
    }

    public void updateLibrary(String name, int id) throws SQLException {
        libraryDAO.update(name , id);
        System.out.println("Library updated: " + name);
    }

    public void deleteLibrary(int id) throws SQLException {
        libraryDAO.delete(id);
        System.out.println("Library deleted with ID: " + id);
    }

    public void updateCGPA(int studentId, float newCGPA) {
        studentDAO.updateCGPA(studentId, newCGPA);
    }

    public void incrementSalary(int teacherId, float incrementAmount) {
        teacherDAO.incrementSalary(teacherId, incrementAmount);
    }

    public void markBookWithCourse(int bookId, int courseId) {
        bookDAO.markBookWithCourse(bookId, courseId);
    }

    public void getTopper() {
        Student st = studentDAO.getTopper();
        System.out.println(st);
    }

    public void getHighestPaidTeacher() {
        Teacher th = teacherDAO.getHighestPaidTeacher();
        System.out.println(th);
    }

    public void getCourseBooks(int courseId) {
        List<Book> books = bookDAO.getBooksForCourse(courseId);
        books.forEach(book -> System.out.println(book));
    }

    public void unmarkBookWithCourse(int bookId, int courseId) {
        bookDAO.unmarkBookWithCourse(bookId, courseId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        studentDAO.addStudentToCourse(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDAO.removeStudentFromCourse(studentId, courseId);
    }

    public void getStudentbooks(int studentId) {
        List<Book> books = studentDAO.getBooksforStudents(studentId);
        books.forEach(book -> System.out.println(book));
    }
}
