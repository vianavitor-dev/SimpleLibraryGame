package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.repository.BookReadHistoryRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentStatsService {
    @Autowired
    private StudentStatsRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookReadHistoryRepository historyRepository;

    @Autowired
    private BookRepository bookRepository;

    public StudentStats create(Long userId) throws ResourceNotFoundException, DuplicateResourceException {
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user must exist to proceed"));

        boolean exists = student.getStats() != null;
        if (exists) {
            throw new DuplicateResourceException("student stats already exists");
        }

        StudentStats stats = new StudentStats();
        stats.setUser(student);
        stats.setLevel(1);
        stats.setMaxLvlExperience(150);
        stats = repository.save(stats);

        student.setStats(stats);
        studentRepository.save(student);

        return stats;
    }

    public StudentStats get(Long id) throws ResourceNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found student"));

        return repository.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("not found student stats"));
    }

    public int calculateAverageReadingTime(Long id, double readingTimeInMins) throws ResourceNotFoundException {
        StudentStats stats = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found student stats"));

        double value = stats.getAverageReadingTime();
        int count = stats.getReadingCount();

        double avg = value + (readingTimeInMins - value)/ (count+1);

        stats.setAverageReadingTime((int) avg);
        repository.save(stats);

        return (int) avg;
    }

    void levelUp(StudentStats stats, int exp) {
        int newLevel = stats.getLevel() + 1;
        int maxLvlExp = stats.getMaxLvlExperience();
        int newMaxLvlExp = maxLvlExp + maxLvlExp/2;

        stats.setLevel(newLevel);
        stats.setCurrentExperience(exp);
        stats.setMaxLvlExperience(newMaxLvlExp);

        repository.save(stats);
    }

    public int setOngoingSteak(Long id) throws ResourceNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found student"));

        StudentStats stats = student.getStats();

        boolean doNotExists = (stats == null);
        if (doNotExists) {
            throw new ResourceNotFoundException("not found student stats");
        }

        LocalDate now = LocalDate.now();
        long differenceInDays = ChronoUnit.DAYS.between(now, student.getLastLogin());

        if (differenceInDays > 0) {
            stats.setOngoingStreak(0);
            return 0;
        }

        stats.setOngoingStreak(stats.getOngoingStreak() + 1);
        repository.save(stats);

        return stats.getOngoingStreak();
    }

    public StudentStats addExp(Long id, int exp) throws ResourceNotFoundException {
        StudentStats stats = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found student stats"));

        int maxLvlExp = stats.getMaxLvlExperience();
        int currentExperience = stats.getCurrentExperience();
        int expRest = (currentExperience + exp) - maxLvlExp;

        boolean doLevelUp = expRest >= 0;

        if (doLevelUp) {
            this.levelUp(stats, expRest);
        } else {
            stats.setCurrentExperience(currentExperience + exp);
            repository.save(stats);
        }

        return stats;
    }

    public void setCurrentBook(Long userId, Long bookId, int page) throws ResourceNotFoundException {
        // TODO: create custom queries to decreased the amount of requests
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("not found student"));

        Optional<BookReadHistory> result = historyRepository.findByStudent(student)
                .stream()
                .filter(history -> Objects.equals(history.getBook().getId(), bookId))
                .findFirst();

        BookReadHistory history = new BookReadHistory();
        boolean isANewBookReading = result.isEmpty();

        if (isANewBookReading) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("not found book"));

            history.setUser(student);
            history.setBook(book);
            history.setLastPageRead(page);
        } else {
            history = result.get();
            history.setLastPageRead(page);
        }

        history.setLastUpdate(LocalDate.now());
        history = historyRepository.save(history);

        student.getStats().setCurrentBook(history);
        repository.save(student.getStats());
    }

    public void delete(Long id) throws ResourceNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found student"));

        student.setStats(null);
        studentRepository.save(student);
//        repository.delete(stats);
    }
}
