package com.vianavitor.simplelibrarygame.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupOfBooksCompKey implements Serializable {
    @Column(name = "book_id")
    Long bookId;

    @Column(name = "group_id")
    Long groupId;

    public GroupOfBooksCompKey() {}

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GroupOfBooksCompKey that = (GroupOfBooksCompKey) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, groupId);
    }
}
