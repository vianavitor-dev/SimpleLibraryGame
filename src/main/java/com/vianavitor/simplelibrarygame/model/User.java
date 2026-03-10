package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.DiscriminatorOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Entity
@Table(name = "`user`")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(columnDefinition = "date not null default (current_date())")
    private LocalDate lastLogin;

    @Column(columnDefinition = "bit(1) not null default b'1'")
    private boolean active;

    @Autowired
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }
}
