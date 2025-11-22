package com.schoolmanagement.models;

public class Book {
    private int id;
    private String bookId;
    private String title;
    private String author;
    private int libraryId;  // Foreign key to Library

    public Book(int id, String bookId, String title, String author, int libraryId) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.libraryId = libraryId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + ", libraryId=" + libraryId + "]";
    }
}
