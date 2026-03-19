package com.vianavitor.simplelibrarygame.model.utils.fields;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupOfBookId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "book_id")
    private Long bookId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Autowired
    public GroupOfBookId() {
    }

    public GroupOfBookId(Long groupId, Long bookId) {
        this.groupId = groupId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GroupOfBookId that = (GroupOfBookId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, bookId);
    }
}
