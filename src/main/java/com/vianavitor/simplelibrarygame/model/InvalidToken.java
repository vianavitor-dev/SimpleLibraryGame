package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class InvalidToken implements Serializable {
    @Id
    private String token;

    @Column(columnDefinition = "date not null default (current_date())")
    private LocalDate expireAt;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDate expireAt) {
        this.expireAt = expireAt;
    }
}
