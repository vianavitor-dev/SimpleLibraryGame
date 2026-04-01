package com.vianavitor.simplelibrarygame.model.utils.classes;

import com.vianavitor.simplelibrarygame.model.Classroom;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Entity
public abstract class UserClassroom extends User {
    @ManyToMany(mappedBy = "usersInClassroom")
    private Set<Classroom> classrooms = new HashSet<>();

    @Autowired
    public UserClassroom() {
    }

    public UserClassroom(String username, String password) {
        super(username, password);
    }

    public Set<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        this.classrooms = classrooms;
    }
}
