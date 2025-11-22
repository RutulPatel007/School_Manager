package com.schoolmanagement.models;

public class Teacher extends Person {
    private String empId;
    private float salary;

    public Teacher(int id, String empId, String name, String dob, String address, float salary) {
        super(id, name, dob, address);
        this.empId = empId;
        this.salary = salary;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public void incrementSalary(float amount) {
        this.salary += amount;
    }

    @Override
    public String toString() {
        return "Teacher [empId=" + empId + ", salary=" + salary + ", name=" + getName() + "]";
    }
}
