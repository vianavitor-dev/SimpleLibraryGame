package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.model.GroupOfBook;
import com.vianavitor.simplelibrarygame.model.utils.fields.GroupOfBookId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupOfBookRepository extends CrudRepository<GroupOfBook, GroupOfBookId> {
    List<GroupOfBook> findByBook(Book book);

    List<GroupOfBook> findByGroup(Group group);
}
