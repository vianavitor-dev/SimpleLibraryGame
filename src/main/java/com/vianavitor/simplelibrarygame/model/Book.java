package com.vianavitor.simplelibrarygame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Nullable
    private String synopsis;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> bookAuthors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> bookGenres = new HashSet<>();

    @Nullable
    private String imagePath;

//    @Nullable
//    private String contentPath;
    private LocalDate releasedAt;

    @Column(columnDefinition = "int not null default 0")
    private int pageCount;

    @Column(columnDefinition = "double not null default 0.0")
    private double ratingValue;

    @Column(columnDefinition = "int not null default 0")
    @JsonIgnore
    private int ratingCount;

    @Column(columnDefinition = "int not null default 1")
    private int quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReadingLevel difficultLevel;

    @Column(columnDefinition = "bit(1) not null default b'1'")
    private boolean available;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(@Nullable String synopsis) {
        this.synopsis = synopsis;
    }

    public Set<Author> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(Set<Author> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public Set<Genre> getBookGenres() {
        return bookGenres;
    }

    public void setBookGenres(Set<Genre> bookGenres) {
        this.bookGenres = bookGenres;
    }

    @Nullable
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

//    @Nullable
//    public String getContentPath() {
//        return contentPath;
//    }
//
//    public void setContentPath(@Nullable String contentPath) {
//        this.contentPath = contentPath;
//    }

    public LocalDate getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(LocalDate releasedAt) {
        this.releasedAt = releasedAt;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ReadingLevel getDifficultLevel() {
        return difficultLevel;
    }

    public void setDifficultLevel(ReadingLevel difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
