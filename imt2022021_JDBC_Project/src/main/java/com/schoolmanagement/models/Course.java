package com.schoolmanagement.models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int courseId;
    private String courseCode;
    private String courseName;
    private String courseDescription;
    private List<Student> students;

    public Course(int courseId, String courseCode, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.students = new ArrayList<>();
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void enrollStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    @Override
    public String toString() {
        return "Course [courseCode=" + courseCode + ", courseName=" + courseName + "]";
    }
}
