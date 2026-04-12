package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.model.GroupOfBook;
import com.vianavitor.simplelibrarygame.model.utils.fields.GroupOfBookId;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.GroupOfBookRepository;
import com.vianavitor.simplelibrarygame.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GroupOfBookService {
    @Autowired
    private GroupOfBookRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GroupRepository groupRepository;

    private GroupOfBook groupOfBook;

    @Autowired
    public GroupOfBookService() {
        this.groupOfBook = new GroupOfBook();
    }

    public GroupOfBookService(
            GroupOfBookRepository repository, BookRepository bookRepository,
            GroupRepository groupRepository, GroupOfBook groupOfBook
    ) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.groupRepository = groupRepository;
        this.groupOfBook = groupOfBook;
    }

    public void addBookToGroup(Long groupId, Long bookId) throws ResourceNotFoundException, DuplicateResourceException {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("not found group"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("not found book"));

        GroupOfBookId id = new GroupOfBookId(groupId, bookId);
        repository.findById(id)
                .ifPresent((x) -> {
                    throw new DuplicateResourceException("book already saved into the group");
                });

        groupOfBook.setBook(book);
        groupOfBook.setGroup(group);
        groupOfBook.setCreatedAt(LocalDate.now());

        repository.save(groupOfBook);
    }

    public void removeBookFromGroup(Long groupId, Long bookId) throws ResourceNotFoundException {
        GroupOfBookId id = new GroupOfBookId(groupId, bookId);
        GroupOfBook groupOfBook = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("this book is not registered in the group"));

        repository.delete(groupOfBook);
    }
}
