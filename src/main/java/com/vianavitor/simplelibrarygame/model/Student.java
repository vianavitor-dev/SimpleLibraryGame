package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("student")
public class Student extends UserClassroom {
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentStatus status;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> favoriteGenre = new HashSet<>();

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public Set<Genre> getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(Set<Genre> favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }
}
