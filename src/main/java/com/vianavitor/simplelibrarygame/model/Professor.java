package com.vianavitor.simplelibrarygame.model;

import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("professor")
public class Professor extends UserClassroom {
}
