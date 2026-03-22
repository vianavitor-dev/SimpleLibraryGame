package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.*;
import com.vianavitor.simplelibrarygame.model.utils.fields.GroupOfBookId;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupOfBookRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private GroupOfBookRepository groupOfBookRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Group testGroup;
    private Book testBook;
    private GroupOfBook testGroupOfBook;

    @BeforeEach
    void setUp() {
        Student student = new Student("group_owner", "pass");
        studentRepository.save(student);

        testGroup = new Group();
        testGroup.setName("Reading Club");
        testGroup.setStudent(student);
        groupRepository.save(testGroup);

        testBook = createBook("Classic Novel");
        bookRepository.save(testBook);

        testGroupOfBook = new GroupOfBook();
        GroupOfBookId id = new GroupOfBookId(testGroup.getId(), testBook.getId());
        testGroupOfBook.setId(id);
        testGroupOfBook.setGroup(testGroup);
        testGroupOfBook.setBook(testBook);
        testGroupOfBook.setCreatedAt(LocalDate.now());
        groupOfBookRepository.save(testGroupOfBook);
    }

    @Test
    void shouldSaveGroupOfBook() {
        Book newBook = createBook("New Book");
        bookRepository.save(newBook);

        GroupOfBook newGroupOfBook = new GroupOfBook();
        GroupOfBookId id = new GroupOfBookId(testGroup.getId(), newBook.getId());
        newGroupOfBook.setId(id);
        newGroupOfBook.setGroup(testGroup);
        newGroupOfBook.setBook(newBook);
        newGroupOfBook.setCreatedAt(LocalDate.now());

        GroupOfBook saved = groupOfBookRepository.save(newGroupOfBook);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getId().getGroupId()).isEqualTo(testGroup.getId());
        assertThat(saved.getId().getBookId()).isEqualTo(newBook.getId());
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldFindGroupOfBookByGroup() {
        List<GroupOfBook> groupBooks = groupOfBookRepository.findByGroup(testGroup);

        assertThat(groupBooks).hasSize(1);
        assertThat(groupBooks.get(0).getBook().getTitle()).isEqualTo("Classic Novel");
    }

    @Test
    void shouldFindGroupOfBookByBook() {
        List<GroupOfBook> groupBooks = groupOfBookRepository.findByBook(testBook);

        assertThat(groupBooks).hasSize(1);
        assertThat(groupBooks.get(0).getGroup().getName()).isEqualTo("Reading Club");
    }

    @Test
    void shouldReturnMultipleBooksForGroup() {
        Book secondBook = createBook("Second Book");
        bookRepository.save(secondBook);

        GroupOfBook secondGroupOfBook = new GroupOfBook();
        GroupOfBookId id = new GroupOfBookId(testGroup.getId(), secondBook.getId());
        secondGroupOfBook.setId(id);
        secondGroupOfBook.setGroup(testGroup);
        secondGroupOfBook.setBook(secondBook);
        secondGroupOfBook.setCreatedAt(LocalDate.now());
        groupOfBookRepository.save(secondGroupOfBook);

        List<GroupOfBook> groupBooks = groupOfBookRepository.findByGroup(testGroup);
        assertThat(groupBooks).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenGroupHasNoBooks() {
        Group newGroup = new Group();
        newGroup.setName("Empty Group");
        newGroup.setStudent(testGroup.getStudent());
        groupRepository.save(newGroup);

        List<GroupOfBook> groupBooks = groupOfBookRepository.findByGroup(newGroup);
        assertThat(groupBooks).isEmpty();
    }

    @Test
    void shouldDeleteGroupOfBook() {
        groupOfBookRepository.deleteById(testGroupOfBook.getId());

        assertThat(groupOfBookRepository.findById(testGroupOfBook.getId())).isEmpty();
    }

    @Test
    void shouldUpdateCreatedAt() {
        LocalDate newDate = LocalDate.of(2024, 1, 1);
        testGroupOfBook.setCreatedAt(newDate);
        groupOfBookRepository.save(testGroupOfBook);

        GroupOfBook updated = groupOfBookRepository.findById(testGroupOfBook.getId()).get();
        assertThat(updated.getCreatedAt()).isEqualTo(newDate);
    }

    private Book createBook(String title) {
        Book book = new Book();
        book.setTitle(title);
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.MEDIUM);
        book.setAvailable(true);
        book.setPageCount(250);
        return book;
    }
}