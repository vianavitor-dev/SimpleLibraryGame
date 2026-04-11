package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@DiscriminatorValue("adm")
public class Administrator extends User {
    @Autowired
    public Administrator() {
    }

    public Administrator(String username, String password) {
        super(username, password);
    }

    public Administrator(String username, String password, String name) {
        super(username, password);
        super.setName(name);
    }
}
