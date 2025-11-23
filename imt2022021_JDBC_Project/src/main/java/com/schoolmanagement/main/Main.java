package com.schoolmanagement.main;

import com.schoolmanagement.models.*;
import com.schoolmanagement.services.SchoolService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// public class Main {
//     public static void main(String[] args) {
//         // Database connection setup
//         String jdbcURL = "jdbc:mysql://localhost:3306/school_db"; // Replace with your database URL
//         String username = "root";  // Replace with your DB username
//         String password = "admin";  // Replace with your DB password

//         try {
//             // Establish the database connection
//             Connection connection = DriverManager.getConnection(jdbcURL, username, password);

//             // Create SchoolService to manage all operations
//             SchoolService schoolService = new SchoolService(connection);

//             // 1. Add a new student
//             // Student student = new Student(1, "S123", "Alice", "2000-01-01", "123 Elm Street", 3.8f);
//             // schoolService.addStudent(student);

//             // // 2. Add a new teacher
//             // Teacher teacher = new Teacher(1, "T123", "Dr. Smith", "1980-01-01", "456 Oak Street", 50000f);
//             // schoolService.addTeacher(teacher);

//             // // 3. Add a new course
//             // Course course = new Course(1, "C123", "Mathematics", "Advanced Math");
//             // schoolService.addCourse(course);

//             // 6. Add a new library
//             Library library = new Library(1, "Central Library");
//             schoolService.addLibrary(library);

//             // 4. Add a new book
//             // Book book = new Book(1, "B123", "Mathematics for Dummies", "John Doe", 1);
//             // System.out.println(book);
//             // schoolService.addBook(book);

//             // // 5. Add book to course
//             // schoolService.markBookWithCourse(1, 1);


//             // // 7. Get topper of the class
//             // schoolService.getTopper();

//             // // 8. Get all students
//             // schoolService.getAllStudents();
            
//             // // // 9. Get all teachers
//             // schoolService.getAllTeachers();

//             // // // 10. Get all courses
//             // schoolService.getAllCourses();

//             // // // 11. Get all books
//             // schoolService.getAllBooks();

//             // // 12. Get all books for a course
//             // schoolService.getCourseBooks(1);

//             // // 16. Enroll student in a course
//             // schoolService.addStudentToCourse(1, 1);
            
//             // // 13. Get all books for a student
            
//             // // // 14. Update student CGPA
//             // schoolService.updateCGPA(1, 4.0f);
            
//             // // // // 15. Update teacher salary
//             // schoolService.incrementSalary(1, 10000f);
            
//             // // 17. Remove student from a course
//             // schoolService.removeStudentFromCourse(1, 1);

//             // schoolService.getStudentbooks(1);

//             // schoolService.getHighestPaidTeacher();

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }




















import com.schoolmanagement.database.DatabaseManager;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static SchoolService schoolService;

    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/school_db"; // Replace with your database URL
        String username = "root";  // Replace with your DB username
        String password = "admin";  // Replace with your DB password
        try {
            // Get the database connection
            // Connection connection = DatabaseManager.getInstance().getConnection();
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);


            // Initialize the service with the connection
            schoolService = new SchoolService(connection);

            // Start CLI Menu
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                showMenu();
                int choice = getUserChoice(scanner);

                switch (choice) {
                    case 1: addStudent(scanner); break;
                    case 2: getStudentById(scanner); break;
                    case 3: updateStudent(scanner); break;
                    case 4: deleteStudent(scanner); break;
                    case 5: addTeacher(scanner); break;
                    case 6: getTeacherById(scanner); break;
                    case 7: updateTeacher(scanner); break;
                    case 8: deleteTeacher(scanner); break;
                    case 9: addCourse(scanner); break;
                    case 10: getCourseById(scanner); break;
                    case 11: updateCourse(scanner); break;
                    case 12: deleteCourse(scanner); break;
                    case 13: addBook(scanner); break;
                    case 14: getBookById(scanner); break;
                    case 15: updateBook(scanner); break;
                    case 16: deleteBook(scanner); break;
                    case 17: addLibrary(scanner); break;
                    case 18: getLibraryById(scanner); break;
                    case 19: updateLibrary(scanner); break;
                    case 20: deleteLibrary(scanner); break;
                    case 21: updateCGPA(scanner); break;
                    case 22: incrementSalary(scanner); break;
                    case 23: markBookWithCourse(scanner); break;
                    case 24: getTopper(); break;
                    case 25: getHighestPaidTeacher(); break;
                    case 26: getCourseBooks(scanner); break;
                    case 27: unmarkBookWithCourse(scanner); break;
                    case 28: addStudentToCourse(scanner); break;
                    case 29: removeStudentFromCourse(scanner); break;
                    case 30: getStudentBooks(scanner); break;
                    case 31: exitApplication(); running = false; break;
                    default: System.out.println("Invalid choice! Please select a valid option.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: Unable to connect to the database.");
            e.printStackTrace();
        }
    }

    // Display Menu
    private static void showMenu() {
        System.out.println("\n====== School Management System ======");
        System.out.println("1. Add Student");
        System.out.println("2. Get Student by ID");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Add Teacher");
        System.out.println("6. Get Teacher by ID");
        System.out.println("7. Update Teacher");
        System.out.println("8. Delete Teacher");
        System.out.println("9. Add Course");
        System.out.println("10. Get Course by ID");
        System.out.println("11. Update Course");
        System.out.println("12. Delete Course");
        System.out.println("13. Add Book");
        System.out.println("14. Get Book by ID");
        System.out.println("15. Update Book");
        System.out.println("16. Delete Book");
        System.out.println("17. Add Library");
        System.out.println("18. Get Library by ID");
        System.out.println("19. Update Library");
        System.out.println("20. Delete Library");
        System.out.println("21. Update Student CGPA");
        System.out.println("22. Increment Teacher Salary");
        System.out.println("23. Mark Book with Course");
        System.out.println("24. Get Topper");
        System.out.println("25. Get Highest Paid Teacher");
        System.out.println("26. Get Books for a Course");
        System.out.println("27. Unmark Book from Course");
        System.out.println("28. Add Student to Course");
        System.out.println("29. Remove Student from Course");
        System.out.println("30. Get Student Books");
        System.out.println("31. Exit");
        System.out.print("Please enter your choice: ");
    }

    // Get user input choice
    private static int getUserChoice(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear the buffer
            return -1; // Invalid input
        }
    }

    // Student Management
    private static void addStudent(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter Student ID:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Roll Number: ");
        String rollNumber = scanner.nextLine();
        System.out.print("Enter Date of Birth (yyyy-mm-dd): ");
        String dob = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter CGPA: ");
        float cgpa = scanner.nextFloat();
        Student student = new Student(id,rollNumber, name, dob, address, cgpa);
        try {
            schoolService.addStudent(student);
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }
    private static void getBookById(Scanner scanner) {
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        try {
            schoolService.getBookById(bookId);
           
        } catch (Exception e) {
            System.out.println("Error fetching book details: " + e.getMessage());
        }
    }
    private static void addLibrary(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.println("Enter Library Id: ");

        int id = scanner.nextInt();
        System.out.println("Enter Library Name: ");
        
        String name = scanner.nextLine();
        Library library = new Library(id, name);
        try {
            schoolService.addLibrary(library);
        } catch (Exception e) {
            System.out.println("Error adding library: " + e.getMessage());
        }
    }
    private static void getStudentById(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        try {
           schoolService.getStudentById(studentId);
          
        } catch (Exception e) {
            System.out.println("Error fetching student details: " + e.getMessage());
        }
    }

    private static void updateStudent(Scanner scanner) {
        System.out.print("Enter Student ID to update: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new CGPA: ");
        float newCGPA = scanner.nextFloat();
        schoolService.updateCGPA(studentId, newCGPA);
        System.out.println("CGPA updated for student ID: " + studentId);
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter Student ID to delete: ");
        int studentId = scanner.nextInt();
        try {
            schoolService.deleteStudent(studentId);
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    // Teacher Management
    private static void addTeacher(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Teacher Id: ");
        int id = scanner.nextInt();
        System.out.print("Enter Teacher Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Employee ID: ");
        String empId = scanner.nextLine();
        System.out.print("Enter Date of Birth (yyyy-mm-dd): ");
        String dob = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Salary: ");
        float salary = scanner.nextFloat();
        Teacher teacher = new Teacher(id,empId, name, dob, address, salary);
        try {
            schoolService.addTeacher(teacher);
        } catch (Exception e) {
            System.out.println("Error adding teacher: " + e.getMessage());
        }
    }

    private static void getTeacherById(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int teacherId = scanner.nextInt();
        try {
            schoolService.getTeacherById(teacherId);
         
        } catch (Exception e) {
            System.out.println("Error fetching teacher details: " + e.getMessage());
        }
    }

    private static void updateTeacher(Scanner scanner) {
        System.out.print("Enter Teacher ID to update: ");
        int teacherId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Salary: ");
        float newSalary = scanner.nextFloat();
        schoolService.incrementSalary(teacherId, newSalary);
        System.out.println("Salary updated for teacher ID: " + teacherId);
    }

    private static void deleteTeacher(Scanner scanner) {
        System.out.print("Enter Teacher ID to delete: ");
        int teacherId = scanner.nextInt();
        try {
            schoolService.deleteTeacher(teacherId);
        } catch (Exception e) {
            System.out.println("Error deleting teacher: " + e.getMessage());
        }
    }

    // Course Management
    private static void addCourse(Scanner scanner) throws Exception {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Course Id: ");
        int id = scanner.nextInt();
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Enter Course Description: ");
        String courseDescription = scanner.nextLine();
        Course course = new Course(id,courseCode, courseName, courseDescription);
        try {
            schoolService.addCourse(course);
        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    private static void getCourseById(Scanner scanner) {
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        try {
            schoolService.getCourseById(courseId);
            
        } catch (Exception e) {
            System.out.println("Error fetching course details: " + e.getMessage());
        }
    }

    private static void updateCourse(Scanner scanner) throws Exception {
        System.out.print("Enter Course Code to update: ");
        String courseId = scanner.next();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter new Course Name: ");
        String newCourseName = scanner.nextLine();
        System.out.print("Enter new Description: ");
        String newDescription = scanner.nextLine();
        schoolService.updateCourse(courseId,newCourseName, newDescription);
        System.out.println("Course updated for ID: " + courseId);
    }

    private static void deleteCourse(Scanner scanner) {
        System.out.print("Enter Course ID to delete: ");
        int courseId = scanner.nextInt();
        try {
            schoolService.deleteCourse(courseId);
        } catch (Exception e) {
            System.out.println("Error deleting course: " + e.getMessage());
        }
    }

    // Exit application
    private static void exitApplication() {
        System.out.println("Exiting the application...");
        System.exit(0);
    }

    // Additional functionalities for books, libraries, etc.
    private static void addBook(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.println("Enter id");
        int id = scanner.nextInt();
        System.out.println("Enter Book Id");
        String book_id = scanner.next();
     
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Library ID: ");
        int libraryId = scanner.nextInt();
        Book book = new Book(id,book_id,title, author, libraryId);
        try {
            schoolService.addBook(book);
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    // Similar implementations for getBookById, updateBook, deleteBook, and other features
    private static void getAllBooks() {
        try {
            schoolService.getAllBooks();
        } catch (Exception e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }

    private static void updateBook(Scanner scanner) throws Exception {
        System.out.print("Enter Book ID to update: ");
        String bookId = scanner.next();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Book Title: ");
        String newTitle = scanner.nextLine();
        System.out.print("Enter new Book Author: ");
        String author = scanner.nextLine();
        schoolService.updateBook(newTitle, author, bookId);
    }

    private static void deleteBook(Scanner scanner) {
        System.out.print("Enter Book ID to delete: ");
        int bookId = scanner.nextInt();
        try {
            schoolService.deleteBook(bookId);
        } catch (Exception e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    // Remaining functionalities like Library, Get Topper, etc., can follow a similar pattern

    private static void getTopper() {
        schoolService.getTopper();
    }

    private static void getHighestPaidTeacher() {
        schoolService.getHighestPaidTeacher();
    }

    private static void getCourseBooks(Scanner scanner) {
        System.out.print("Enter Course ID to get books: ");
        int courseId = scanner.nextInt();
        schoolService.getCourseBooks(courseId);
    }

    private static void unmarkBookWithCourse(Scanner scanner) {
        System.out.print("Enter Book ID to unmark: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        schoolService.unmarkBookWithCourse(bookId, courseId);
    }

   private static void addStudentToCourse(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();

        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();

        try {
            schoolService.addStudentToCourse(studentId, courseId);
            System.out.println("Student added to course successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding student to course: " + e.getMessage());
        }
    }


    private static void removeStudentFromCourse(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        schoolService.removeStudentFromCourse(studentId, courseId);
    }

    private static void getStudentBooks(Scanner scanner) {
        System.out.print("Enter Student ID to get books: ");
        int studentId = scanner.nextInt();
        schoolService.getStudentBooks(studentId);
    }

    private static void getLibraryById(Scanner scanner) {
        System.out.print("Enter Library ID: ");
        int libraryId = scanner.nextInt();
    
        try {
            schoolService.getLibraryById(libraryId);
        } catch (Exception e) {
            System.out.println("Error fetching library details: " + e.getMessage());
        }
    }

    private static void updateLibrary(Scanner scanner) {
        System.out.print("Enter Library ID to update: ");
        int libraryId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        System.out.print("Enter new Library Name: ");
        String newName = scanner.nextLine();
        
        try {
            schoolService.updateLibrary(libraryId, newName);
            System.out.println("Library updated: " + newName);
        } catch (Exception e) {
            System.out.println("Error updating library: " + e.getMessage());
        }
    }

    private static void deleteLibrary(Scanner scanner) {
        System.out.print("Enter Library ID to delete: ");
        int libraryId = scanner.nextInt();
    
        try {
            schoolService.deleteLibrary(libraryId);
            System.out.println("Library deleted with ID: " + libraryId);
        } catch (Exception e) {
            System.out.println("Error deleting library: " + e.getMessage());
        }
    }

    private static void updateCGPA(Scanner scanner) {
        System.out.print("Enter Student ID to update CGPA: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter new CGPA: ");
        float newCGPA = scanner.nextFloat();
    
        try {
            schoolService.updateCGPA(studentId, newCGPA);
            System.out.println("CGPA updated for student ID: " + studentId);
        } catch (Exception e) {
            System.out.println("Error updating CGPA: " + e.getMessage());
        }
    }

    private static void incrementSalary(Scanner scanner) {
        System.out.print("Enter Teacher ID to increment salary: ");
        int teacherId = scanner.nextInt();
        System.out.print("Enter salary increment amount: ");
        float incrementAmount = scanner.nextFloat();
    
        try {
            schoolService.incrementSalary(teacherId, incrementAmount);
            System.out.println("Salary incremented for Teacher ID: " + teacherId);
        } catch (Exception e) {
            System.out.println("Error incrementing salary: " + e.getMessage());
        }
    }

    private static void markBookWithCourse(Scanner scanner) {
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
    
        try {
            schoolService.markBookWithCourse(bookId, courseId);
            System.out.println("Book marked with Course ID: " + courseId);
        } catch (Exception e) {
            System.out.println("Error marking book with course: " + e.getMessage());
        }
    }
    
    
    
    
    
    
}
