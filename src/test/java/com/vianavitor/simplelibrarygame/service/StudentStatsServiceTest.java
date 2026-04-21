package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import com.vianavitor.simplelibrarygame.repository.BookReadHistoryRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class StudentStatsServiceTest extends BaseServiceTest {

    @Mock
    private StudentStatsRepository repository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BookReadHistoryRepository historyRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private StudentStatsService service;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;

    @Captor
    private ArgumentCaptor<StudentStats> statsCaptor;

    @Captor
    private ArgumentCaptor<BookReadHistory> historyCaptor;

    private Student testStudent;
    private StudentStats testStats;
    private Book testBook;
    private BookReadHistory testHistory;

    @BeforeEach
    void setUp() {
        testStudent = new Student("stats_student", "pass");
        testStudent.setName("Stats Student");

        testStats = new StudentStats();
//        testStats.setUser(testStudent);
        testStats.setLevel(5);
        testStats.setCurrentExperience(250);
        testStats.setMaxLvlExperience(500);
        testStats.setOngoingStreak(3);
        testStats.setAverageReadingTime(30);
        testStats.setReadingCount(10);

//        testStudent.setStats(testStats);

        testBook = new Book();
        testBook.setTitle("Book title");
        testBook.setReleasedAt(LocalDate.now());
        testBook.setDifficultLevel(ReadingLevel.EASY);
        testBook.setAvailable(true);
        testBook.setPageCount(200);

        testHistory = new BookReadHistory();
        testHistory.setUser(testStudent);
        testHistory.setBook(testBook);
        testHistory.setLastPageRead(50);
        testHistory.setLastUpdate(LocalDate.now());
    }

    @Test
    void testCreate() {
        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(null);

        StudentStats result = service.create(1L);

        Mockito.verify(studentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).save(statsCaptor.capture());
        Mockito.verify(studentRepository, Mockito.times(1)).save(studentCaptor.capture());

        Student savedStudent = studentCaptor.getValue();
        StudentStats savedStats = statsCaptor.getValue();

        assertThat(result).isNotNull();
        assertThat(result.getStudent()).isNotNull();
        assertThat(result.getStudent()).withFailMessage("different objects (Student)").isEqualTo(testStudent);
        assertThat(savedStudent.getStats()).isNotNull();
        assertThat(savedStudent.getStats()).withFailMessage("different objects (StudentStats)").isEqualTo(result);
        assertThat(savedStats.getLevel()).isEqualTo(1);
        assertThat(savedStats.getMaxLvlExperience()).isEqualTo(150);
    }

    @Test
    void testGet() {
        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        Mockito.when(repository.findByStudent(Mockito.any(Student.class)))
                .thenReturn(Optional.of(testStats));

        StudentStats result = service.get(1L);

        assertThat(result).isNotNull();
        assertThat(result.getLevel()).isEqualTo(5);

        Mockito.verify(studentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).findByStudent(testStudent);
    }

    @Test
    void testCalculateAverageReadingTime() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(testStats));

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenReturn(testStats);

        int result = service.calculateAverageReadingTime(1L, 57.2);

        assertThat(result).isEqualTo(32);

        Mockito.verify(repository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).save(testStats);
    }

    @Test
    void testSetOngoingSteak() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        testStudent.setLastLogin(yesterday);
        testStudent.setStats(testStats);

        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenReturn(testStats);

        int ongoingStack = service.setOngoingSteak(1L);

        assertThat(ongoingStack).isEqualTo(4);

        Mockito.verify(studentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).save(testStats);
    }

    @Test
    void shouldNotLevelUpWhenAddingExp() {
        int previousExp = testStats.getCurrentExperience();
        int previousLvl = testStats.getLevel();
        int maxExp = 500;
        int addExp = 100;
        int expRest = (previousExp + addExp) - maxExp;

        boolean doLevelUp = expRest >= 0;

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(testStats));

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenReturn(testStats);

        StudentStats result = service.addExp(1L, addExp);

        assertThat(doLevelUp).isFalse();
        assertThat(result.getLevel()).isEqualTo(previousLvl);
        assertThat(result.getCurrentExperience()).isGreaterThan(previousExp);
        assertThat(result.getCurrentExperience()).isEqualTo(previousExp + addExp);

        Mockito.verify(repository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).save(testStats);
    }

    @Test
    void shouldLevelUpWhenAddingExp() {
        int previousExp = testStats.getCurrentExperience();
        int maxExp = 500;
        int addExp = 251;
        int expRest = (previousExp + addExp) - maxExp;

        boolean doLevelUp = expRest >= 0;
        int expectedLvl = testStats.getLevel() + 1;

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(testStats));

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenReturn(testStats);

        StudentStats result = service.addExp(1L, addExp);

        assertThat(doLevelUp).isTrue();
        assertThat(result.getLevel()).isEqualTo(expectedLvl);
        assertThat(result.getCurrentExperience()).isNotEqualTo(previousExp);
        assertThat(result.getCurrentExperience()).isEqualTo((previousExp + addExp) % maxExp);

        Mockito.verify(repository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).save(testStats);
    }

    @Test
    void testSetCurrentBookForTheFirstTime() {
        testStudent.setId(1L);
        testBook.setId(1L);

        testStudent.setStats(testStats);

        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        Mockito.when(historyRepository.findByStudent(Mockito.any(Student.class)))
                .thenReturn(List.of());

        Mockito.when(historyRepository.save(Mockito.any(BookReadHistory.class)))
                .thenReturn(testHistory);

        Mockito.when(bookRepository.findById(1L))
                .thenReturn(Optional.of(testBook));

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenReturn(null);

        service.setCurrentBook(1L, 1L, 55);

        Mockito.verify(studentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(historyRepository, Mockito.times(1)).findByStudent(testStudent);
        Mockito.verify(historyRepository, Mockito.times(1)).save(historyCaptor.capture());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(repository, Mockito.times(1)).save(testStats);

        BookReadHistory savedHistory = historyCaptor.getValue();

        assertThat(testStats.getCurrentBook()).isNotNull();
        assertThat(testStats.getCurrentBook()).withFailMessage("different book").isEqualTo(testHistory);
        assertThat(savedHistory.getLastPageRead()).isEqualTo(55);
        assertThat(savedHistory.getLastUpdate()).isToday();
        assertThat(savedHistory.getBook()).withFailMessage("different book").isEqualTo(testBook);
    }

    @Test
    void testSetCurrentBook() {
        testStudent.setId(1L);
        testBook.setId(1L);

        testStudent.setStats(testStats);

        List<BookReadHistory> list = new ArrayList<>();
        list.add(testHistory);

        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        Mockito.when(historyRepository.findByStudent(Mockito.any(Student.class)))
                .thenReturn(list);

        Mockito.when(historyRepository.save(Mockito.any(BookReadHistory.class)))
                .thenReturn(testHistory);

        Mockito.when(repository.save(Mockito.any(StudentStats.class)))
                .thenReturn(null);

        service.setCurrentBook(1L, 1L, 55);

        assertThat(testStats.getCurrentBook()).isEqualTo(testHistory);

        Mockito.verify(studentRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(historyRepository, Mockito.times(1)).findByStudent(testStudent);
        Mockito.verify(historyRepository, Mockito.times(1)).save(testHistory);
        Mockito.verify(repository, Mockito.times(1)).save(testStats);
    }
}