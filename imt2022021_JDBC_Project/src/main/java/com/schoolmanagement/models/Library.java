package com.schoolmanagement.models;

import java.util.List;

public class Library {
    private int id;
    private String name;

    public Library(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Library [id=" + id + ", name=" + name + "]";
    }
}
