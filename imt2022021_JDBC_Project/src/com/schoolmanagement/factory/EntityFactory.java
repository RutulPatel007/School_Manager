package com.schoolmanagement.factory;

import com.schoolmanagement.models.*;

public class EntityFactory {

    // Factory method to create entities based on the type provided
    public static Object createEntity(String entityType) {
        switch (entityType) {
            case "Student":
                return new Student(0, "", "", "", "", 0.0f); // Creating a default student instance
            case "Teacher":
                return new Teacher(0, "", "", "", "", 0.0f); // Creating a default teacher instance
            case "Course":
                return new Course(0, "", "", ""); // Creating a default course instance
            case "Book":
                return new Book(0, "", "", "", 0); // Creating a default book instance
            case "Library":
                return new Library(0, ""); // Creating a default library instance
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }

    // Factory method to create entities based on an enum (optional but cleaner)
    public static Object createEntity(EntityType entityType) {
        switch (entityType) {
            case STUDENT:
                return new Student(0, "", "", "", "", 0.0f);
            case TEACHER:
                return new Teacher(0, "", "", "", "", 0.0f);
            case COURSE:
                return new Course(0, "", "", "");
            case BOOK:
                return new Book(0, "", "", "", 0);
            case LIBRARY:
                return new Library(0, "");
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }

    // Enum to represent entity types (optional)
    public enum EntityType {
        STUDENT,
        TEACHER,
        COURSE,
        BOOK,
        LIBRARY
    }
}
