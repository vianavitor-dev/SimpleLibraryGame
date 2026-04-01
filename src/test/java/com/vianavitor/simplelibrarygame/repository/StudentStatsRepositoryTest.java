package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.*;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StudentStatsRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private StudentStatsRepository statsRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookReadHistoryRepository historyRepository;

    private Student testStudent;
    private StudentStats testStats;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testStudent = new Student("stats_student", "pass");
        testStudent.setName("Stats Student");
        studentRepository.save(testStudent);

        testStats = new StudentStats();
        testStats.setUser(testStudent);
        testStats.setLevel(5);
        testStats.setCurrentExperience(250);
        testStats.setMaxLvlExperience(500);
        testStats.setOngoingStreak(3);
        testStats.setAverageReadingTime(30);
        testStats.setReadingCount(10);
        statsRepository.save(testStats);

        testStudent.setStats(testStats);
        studentRepository.save(testStudent);

        testBook = createBook("Stats Test Book");
        bookRepository.save(testBook);
    }

    @Test
    void shouldSaveStudentStats() {
        Student newStudent = new Student("new_stats_student", "pass");
        studentRepository.save(newStudent);

        StudentStats newStats = new StudentStats();
        newStats.setUser(newStudent);
        newStats.setLevel(1);
        newStats.setCurrentExperience(0);
        newStats.setMaxLvlExperience(100);

        StudentStats saved = statsRepository.save(newStats);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getId()).isEqualTo(newStudent.getId());
        assertThat(saved.getLevel()).isEqualTo(1);
    }

    @Test
    void shouldFindStudentStatsByStudent() {
        Optional<StudentStats> found = statsRepository.findByStudent(testStudent);

        assertThat(found).isPresent();
        assertThat(found.get().getLevel()).isEqualTo(5);
        assertThat(found.get().getCurrentExperience()).isEqualTo(250);
        assertThat(found.get().getOngoingStreak()).isEqualTo(3);
    }

    @Test
    void shouldUpdateStats() {
        testStats.setLevel(6);
        testStats.setCurrentExperience(50);
        testStats.setOngoingStreak(4);
        statsRepository.save(testStats);

        StudentStats updated = statsRepository.findById(testStats.getId()).get();
        assertThat(updated.getLevel()).isEqualTo(6);
        assertThat(updated.getCurrentExperience()).isEqualTo(50);
        assertThat(updated.getOngoingStreak()).isEqualTo(4);
    }

    @Test
    void shouldFindByCurrentBook() {
        BookReadHistory history = new BookReadHistory();
        history.setUser(testStudent);
        history.setBook(testBook);
        history.setLastPageRead(100);
        historyRepository.save(history);

        testStats.setCurrentBook(history);
        statsRepository.save(testStats);

        List<StudentStats> statsWithBook = statsRepository.findByCurrentBook(history);

        assertThat(statsWithBook).hasSize(1);
        assertThat(statsWithBook.get(0).getStudent().getId()).isEqualTo(testStudent.getId());
    }

    @Test
    void shouldUpdateCurrentBook() {
        BookReadHistory history = new BookReadHistory();
        history.setUser(testStudent);
        history.setBook(testBook);
        history.setLastPageRead(50);
        historyRepository.save(history);

        testStats.setCurrentBook(history);
        statsRepository.save(testStats);

        Optional<StudentStats> updated = statsRepository.findById(testStats.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getCurrentBook()).isNotNull();
        assertThat(updated.get().getCurrentBook().getId()).isEqualTo(history.getId());
    }

    @Test
    void shouldHandleLevelUp() {
        testStats.setCurrentExperience(550);
        testStats.setLevel(5);
        statsRepository.save(testStats);

        // Simulate level up
        if (testStats.getCurrentExperience() >= testStats.getMaxLvlExperience()) {
            testStats.setLevel(testStats.getLevel() + 1);
            testStats.setCurrentExperience(testStats.getCurrentExperience() - testStats.getMaxLvlExperience());
            testStats.setMaxLvlExperience(testStats.getMaxLvlExperience() + 50);
            statsRepository.save(testStats);
        }

        StudentStats updated = statsRepository.findById(testStats.getId()).get();
        assertThat(updated.getLevel()).isEqualTo(6);
        assertThat(updated.getCurrentExperience()).isEqualTo(550 - 500);
        assertThat(updated.getMaxLvlExperience()).isEqualTo(550);
    }

    @Test
    void shouldIncrementReadingCount() {
        int initialCount = testStats.getReadingCount();
        testStats.setReadingCount(initialCount + 1);
        statsRepository.save(testStats);

        StudentStats updated = statsRepository.findById(testStats.getId()).get();
        assertThat(updated.getReadingCount()).isEqualTo(initialCount + 1);
    }

    private Book createBook(String title) {
        Book book = new Book();
        book.setTitle(title);
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.EASY);
        book.setAvailable(true);
        book.setPageCount(200);
        return book;
    }
}