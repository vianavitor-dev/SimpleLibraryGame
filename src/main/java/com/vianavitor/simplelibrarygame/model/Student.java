package com.vianavitor.simplelibrarygame.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("student")
public class Student extends User {
    @ManyToOne
    @JoinColumn(name = "student_level_status_id")
    private StudentLvlStatus status;

    public StudentLvlStatus getStatus() {
        return status;
    }

    public void setStatus(StudentLvlStatus status) {
        this.status = status;
    }
}
