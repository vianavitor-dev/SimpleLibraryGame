package com.vianavitor.simplelibrarygame.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class GroupOfBookId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "book_id")
    private Long bookId;
}
