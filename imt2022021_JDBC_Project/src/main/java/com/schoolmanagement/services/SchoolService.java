package com.schoolmanagement.services;

import com.schoolmanagement.dao.*;
import com.schoolmanagement.models.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SchoolService {

    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final CourseDAO courseDAO;
    private final BookDAO bookDAO;
    private final LibraryDAO libraryDAO;

    public SchoolService(Connection connection) {
        this.studentDAO = new StudentDAO(connection);
        this.teacherDAO = new TeacherDAO(connection);
        this.courseDAO = new CourseDAO(connection);
        this.bookDAO = new BookDAO(connection);
        this.libraryDAO = new LibraryDAO(connection);
    }

    // ------------------ STUDENTS ------------------

    public void addStudent(Student student) throws SQLException {
        studentDAO.create(student);
        System.out.println("Student added: " + student.getName());
    }

    public void getStudentById(int id) throws SQLException {
        System.out.println(studentDAO.read(id));
    }

    public void getAllStudents() throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        students.forEach(System.out::println);
    }

    public void updateStudentAddress(String rollNumber, String address) throws SQLException {
        studentDAO.updateAddress(rollNumber, address);
        System.out.println("Student Address Updated");
    }

    public void deleteStudent(int id) throws SQLException {
        studentDAO.delete(id);
        System.out.println("Student deleted with ID: " + id);
    }

    // ------------------ TEACHERS ------------------

    public void addTeacher(Teacher teacher) throws SQLException {
        teacherDAO.create(teacher);
        System.out.println("Teacher added: " + teacher.getName());
    }

    public void getTeacherById(int id) throws SQLException {
        System.out.println(teacherDAO.read(id));
    }

    public void getAllTeachers() throws SQLException {
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        teachers.forEach(System.out::println);
    }

    public void updateTeacherAddress(String empId, String newAddress) throws SQLException {
        teacherDAO.updateAddress(empId, newAddress);
        System.out.println("Teacher updated");
    }

    public void deleteTeacher(int id) throws SQLException {
        teacherDAO.delete(id);
        System.out.println("Teacher deleted with ID: " + id);
    }

    // ------------------ COURSES ------------------

    public void addCourse(Course course) throws SQLException {
        courseDAO.create(course);
        System.out.println("Course added: " + course.getCourseName());
    }

    public void getCourseById(int id) throws SQLException {
        System.out.println(courseDAO.read(id));
    }

    public void getAllCourses() throws SQLException {
        List<Course> courses = courseDAO.getAllCourses();
        courses.forEach(System.out::println);
    }

    public void updateCourse(String code, String name, String desc) throws SQLException {
        courseDAO.update(code, name, desc);
        System.out.println("Course updated");
    }

    public void deleteCourse(int id) throws SQLException {
        courseDAO.delete(id);
        System.out.println("Course deleted with ID: " + id);
    }

    // ------------------ LIBRARIES & BOOKS ------------------

    public void addBook(Book book) throws SQLException {
        bookDAO.create(book);
        System.out.println("Book added: " + book.getTitle());
    }

    public void getBookById(int id) throws SQLException {
        System.out.println(bookDAO.read(id));
    }

    public void getAllBooks() throws SQLException {
        List<Book> books = bookDAO.getAllBooks();
        books.forEach(System.out::println);
    }

    public void updateBook(String bookId, String title, String author) throws SQLException {
        bookDAO.update(bookId, title, author);
        System.out.println("Book updated: " + title);
    }

    public void deleteBook(int id) throws SQLException {
        bookDAO.delete(id);
        System.out.println("Book deleted with ID: " + id);
    }

    public void addLibrary(Library library) throws SQLException {
        libraryDAO.create(library);
        System.out.println("Library added: " + library.getName());
    }

    public void getLibraryById(int id) throws SQLException {
        System.out.println(libraryDAO.read(id));
    }

    public void getAllLibraries() throws SQLException {
        List<Library> libs = libraryDAO.getAllLibraries();
        libs.forEach(System.out::println);
    }

    public void updateLibrary(int id, String name) throws SQLException {
        libraryDAO.update(id, name);
        System.out.println("Library updated: " + name);
    }

    public void deleteLibrary(int id) throws SQLException {
        libraryDAO.delete(id);
        System.out.println("Library deleted with ID: " + id);
    }

    // ------------------ ADVANCED OPERATIONS ------------------

    public void updateCGPA(int studentId, float newCGPA) {
        studentDAO.updateCGPA(studentId, newCGPA);
    }

    public void incrementSalary(int teacherId, float incrementAmount) {
        teacherDAO.incrementSalary(teacherId, incrementAmount);
    }

    public void markBookWithCourse(int bookId, int courseId) {
        bookDAO.markBookWithCourse(bookId, courseId);
    }

    public void unmarkBookWithCourse(int bookId, int courseId) {
        bookDAO.unmarkBookWithCourse(bookId, courseId);
    }

    public void addStudentToCourse(int studentId, int courseId) throws SQLException {
        studentDAO.addStudentToCourse(studentId, courseId);
    }
    

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDAO.removeStudentFromCourse(studentId, courseId);
    }

    public void getCourseBooks(int courseId) {
        bookDAO.getBooksForCourse(courseId).forEach(System.out::println);
    }

    public void getStudentBooks(int studentId) {
        studentDAO.getBooksForStudent(studentId).forEach(System.out::println);
    }

    public void getTopper() {
        System.out.println(studentDAO.getTopper());
    }

    public void getHighestPaidTeacher() {
        System.out.println(teacherDAO.getHighestPaidTeacher());
    }
}
