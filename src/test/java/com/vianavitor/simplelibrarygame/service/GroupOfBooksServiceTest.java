package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.model.GroupOfBook;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.utils.fields.GroupOfBookId;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.GroupOfBookRepository;
import com.vianavitor.simplelibrarygame.repository.GroupRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GroupOfBooksServiceTest extends BaseServiceTest {
    @Mock
    private GroupOfBookRepository repository;
    
    @Mock
    private GroupRepository groupRepository;
    
    @Mock
    private BookRepository bookRepository;

    private Book testBook;
    private Group testGroup;
    private GroupOfBook testGroupOfBook;

    @BeforeEach
    void setUp() {
        Student student = new Student("group_owner", "pass");

        testGroup = new Group();
        testGroup.setName("Reading Club");
        testGroup.setStudent(student);

        testBook = new Book();
        testBook.setTitle("Classic Novel");
        testBook.setReleasedAt(LocalDate.now());
        testBook.setDifficultLevel(ReadingLevel.MEDIUM);
        testBook.setAvailable(true);
        testBook.setPageCount(250);
        
        GroupOfBookId id = new GroupOfBookId(testGroup.getId(), testBook.getId());

        testGroupOfBook = new GroupOfBook();
        testGroupOfBook.setId(id);
    }

    @Test
    public void testAddBookToGroup() {
        when(groupRepository.findById(1L))
                .thenReturn(Optional.of(testGroup));

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.findById(any(GroupOfBookId.class)))
                .thenReturn(Optional.empty());

        when(repository.save(testGroupOfBook))
                .thenReturn(null);

        GroupOfBookService service = new GroupOfBookService(
                repository, bookRepository, groupRepository, testGroupOfBook
        );
        service.addBookToGroup(1L, 1L);

        assertThat(testGroupOfBook.getBook()).isNotNull();
        assertThat(testGroupOfBook.getBook()).isNotNull();
        assertThat(testGroupOfBook.getCreatedAt()).isToday();

        verify(groupRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(repository, times(1)).save(testGroupOfBook);
    }

    @Test
    public void testAddBookToGroupDuplicateBookInGroupException() {
        when(groupRepository.findById(1L))
                .thenReturn(Optional.of(testGroup));

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.findById(any(GroupOfBookId.class)))
                .thenReturn(Optional.of(Mockito.mock(GroupOfBook.class)));

        GroupOfBookService service = new GroupOfBookService(
                repository, bookRepository, groupRepository, testGroupOfBook
        );

        assertThrows(Exception.class, () -> {
            service.addBookToGroup(1L, 1L);
        });

        verify(groupRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
    }
}
