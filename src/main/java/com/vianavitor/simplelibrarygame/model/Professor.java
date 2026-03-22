package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@DiscriminatorValue("professor")
public class Professor extends UserClassroom {
    @Autowired
    public Professor() {
    }

    public Professor(String username, String password) {
        super(username, password);
    }
}
