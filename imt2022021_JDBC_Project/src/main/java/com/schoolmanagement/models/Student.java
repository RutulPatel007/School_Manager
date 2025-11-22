package com.schoolmanagement.models;

public class Student extends Person {
    private String rollNumber;
    private float cgpa;

    public Student(int id, String rollNumber, String name, String dob, String address, float cgpa) {
        super(id, name, dob, address);
        this.rollNumber = rollNumber;
        this.cgpa = cgpa;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public float getCgpa() {
        return cgpa;
    }

    public void setCgpa(float cgpa) {
        this.cgpa = cgpa;
    }

    public void updateCGPA(float newCgpa) {
        this.cgpa = newCgpa;
    }

    @Override
    public String toString() {
        return "Student [rollNumber=" + rollNumber + ", cgpa=" + cgpa + ", name=" + getName() + "]";
    }
}
