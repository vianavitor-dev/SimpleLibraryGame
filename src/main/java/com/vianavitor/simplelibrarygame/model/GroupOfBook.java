package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "group_book")
public class GroupOfBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroups() {
        return group;
    }

    public void setGroups(Group group) {
        this.group = group;
    }

    public Book getBooks() {
        return book;
    }

    public void setBooks(Book book) {
        this.book = book;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
