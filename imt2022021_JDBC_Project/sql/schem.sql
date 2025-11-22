-- Create the school database
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

-- Table for Students
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    address VARCHAR(255),
    cgpa FLOAT NOT NULL
);

-- Table for Teachers
CREATE TABLE IF NOT EXISTS teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    address VARCHAR(255),
    salary FLOAT NOT NULL
);

-- Table for Courses
CREATE TABLE IF NOT EXISTS courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    course_description TEXT
);
-- Table for Libraries (One-to-Many with Books)
CREATE TABLE IF NOT EXISTS libraries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Table for Books (One-to-Many with Library)
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    library_id INT,
    FOREIGN KEY (library_id) REFERENCES libraries(id) ON DELETE CASCADE
);


-- Table for Enrollments (Many-to-Many between Students and Courses)
CREATE TABLE IF NOT EXISTS enrollments (
    student_id INT,
    course_id INT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Table for Course-Teacher Relationship (Many-to-Many between Courses and Teachers)
CREATE TABLE IF NOT EXISTS course_teacher (
    course_id INT,
    teacher_id INT,
    PRIMARY KEY (course_id, teacher_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);

-- Table for Course-Book Relationship (Many-to-Many between Courses and Books)
CREATE TABLE IF NOT EXISTS course_books (
    course_id INT,
    book_id INT,
    PRIMARY KEY (course_id, book_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
