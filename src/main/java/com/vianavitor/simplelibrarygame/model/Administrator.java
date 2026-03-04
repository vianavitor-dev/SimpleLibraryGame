package com.vianavitor.simplelibrarygame.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("1")
public class Administrator extends User {
}
