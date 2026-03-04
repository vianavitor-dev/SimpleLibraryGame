package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.utils.Language;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Nullable
    private String description;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @Nullable
    private String imagePath;

    @Nullable
    private String contentPath;
    private LocalDate releasedAt;
    private String publishedBy;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(columnDefinition = "0")
    private short lastPageRead;

    @Column(columnDefinition = "0")
    private short reportedCount;

    @Column(columnDefinition = "0")
    private short pageCount;

    @Column(columnDefinition = "0")
    private double ratingValue;

    @Column(columnDefinition = "0")
    private int ratingCount;

    @Column(columnDefinition = "1")
    private int quantity;
    private boolean active = true;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Genre> getGenrer() {
        return genres;
    }

    public void setGenrer(List<Genre> genres) {
        this.genres = genres;
    }

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
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

    @Nullable
    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(@Nullable String contentPath) {
        this.contentPath = contentPath;
    }

    public LocalDate getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(LocalDate releasedAt) {
        this.releasedAt = releasedAt;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public short getLastPageRead() {
        return lastPageRead;
    }

    public void setLastPageRead(short lastPageRead) {
        this.lastPageRead = lastPageRead;
    }

    public short getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(short reportedCount) {
        this.reportedCount = reportedCount;
    }

    public short getPageCount() {
        return pageCount;
    }

    public void setPageCount(short pageCount) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
