package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("student")
public class Student extends UserClassroom {
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentStats stats;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> favoriteGenre = new HashSet<>();

    @Autowired
    public Student() {
    }

    public Student(String username, String password) {
        super(username, password);
    }

    public StudentStats getStats() {
        return stats;
    }

    public void setStats(StudentStats stats) {
        this.stats = stats;
    }

    public Set<Genre> getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(Set<Genre> favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }
}
