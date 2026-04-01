package com.vianavitor.simplelibrarygame.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
public class StudentStats {
    @Id
    private Long id;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "book_read_hisory_id")
    private BookReadHistory currentBook;    // it saves only the history of the last book read by the user

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    @JoinColumn(name = "user_id")
    private Student student;

    @Column(columnDefinition = "int not null default 1")
    private int level;

    @Column(name = "experience", columnDefinition = "int not null default 0")
    private int currentExperience;

    @Column(columnDefinition = "int not null default 100")
    private int maxLvlExperience;

    @Column(columnDefinition = "int not null default 0")
    private int ongoingStreak;

    @Column(columnDefinition = "int not null default 0")
    private int averageReadingTime;

    @Column(columnDefinition = "int not null default 0")
    private int readingCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public BookReadHistory getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(@Nullable BookReadHistory currentBook) {
        this.currentBook = currentBook;
    }

    public Student getStudent() {
        return student;
    }

    public void setUser(Student student) {
        this.student = student;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
    }

    public int getMaxLvlExperience() {
        return maxLvlExperience;
    }

    public void setMaxLvlExperience(int maxLvlExperience) {
        this.maxLvlExperience = maxLvlExperience;
    }

    public int getOngoingStreak() {
        return ongoingStreak;
    }

    public void setOngoingStreak(int ongoingStreak) {
        this.ongoingStreak = ongoingStreak;
    }

    public int getAverageReadingTime() {
        return averageReadingTime;
    }

    public void setAverageReadingTime(int averageReadingTime) {
        this.averageReadingTime = averageReadingTime;
    }

    public int getReadingCount() {
        return readingCount;
    }

    public void setReadingCount(int readingCount) {
        this.readingCount = readingCount;
    }
}
