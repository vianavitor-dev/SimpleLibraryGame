package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("librarian")
public class Librarian extends User {
    @Autowired
    public Librarian() {
    }

    public Librarian(String username, String password) {
        super(username, password);
    }
}
