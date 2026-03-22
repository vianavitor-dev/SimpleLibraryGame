package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class BookReadHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(columnDefinition = "int not null default 0")
    private int lastPageRead;

    @Column(nullable = false)
    private LocalDate lastUpdate = LocalDate.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getUser() {
        return student;
    }

    public void setUser(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getLastPageRead() {
        return lastPageRead;
    }

    public void setLastPageRead(int lastPageRead) {
        this.lastPageRead = lastPageRead;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
