package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("adm")
public class Administrator extends User {
}
