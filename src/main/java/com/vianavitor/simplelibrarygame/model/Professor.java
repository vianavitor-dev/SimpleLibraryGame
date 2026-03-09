package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("professor")
public class Professor extends User {
}
