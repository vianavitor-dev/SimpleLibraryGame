package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("librarian")
public class Librarian extends User {
}
