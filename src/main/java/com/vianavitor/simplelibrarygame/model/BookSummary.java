package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "book_summary")
public class BookSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private String text;

    @Column(columnDefinition = "date not null default (current_date())")
    private LocalDate writtenAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getWrittenAt() {
        return writtenAt;
    }

    public void setWrittenAt(LocalDate writtenAt) {
        this.writtenAt = writtenAt;
    }
}
