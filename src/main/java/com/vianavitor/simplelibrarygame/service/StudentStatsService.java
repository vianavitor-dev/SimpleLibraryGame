package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class StudentStatsService {
    @Autowired
    private StudentStatsRepository repository;

    @Autowired
    private StudentRepository studentRepository;

//    lvl up: lvl+1 & exp 0 & max-exp + max-exp*0.5
//    submit book summary: exp + N & ongoing-streak + 1

    public StudentStats create(Long userId) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found student"));

        boolean exists = student.getStats() != null;
        if (exists) {
            throw new RuntimeException("student stats already exists");
        }

        StudentStats stats = new StudentStats();

        student.setStats(stats);
        studentRepository.save(student);

        return repository.save(stats);
    }

    public StudentStats get(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found student"));

        return repository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("not found student stats"));
    }

    public void calculateAverageReadingTime(Long id, double readingTimeInMins) {
        StudentStats stats = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found student stats"));

        double value = stats.getAverageReadingTime();
        int count = stats.getReadingCount();

        double avg = value + (readingTimeInMins - value)/ (count+1);

        stats.setAverageReadingTime((int) avg);
        repository.save(stats);
    }

    private void levelUp(StudentStats stats, int exp) {
        int newLevel = stats.getLevel() + 1;
        int maxLvlExp = stats.getMaxLvlExperience();
        int newMaxLvlExp = maxLvlExp + maxLvlExp/2;

        stats.setLevel(newLevel);
        stats.setCurrentExperience(exp);
        stats.setMaxLvlExperience(newMaxLvlExp);

        repository.save(stats);
    }

    public int setOngoingSteak(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found student"));

        StudentStats stats = student.getStats();

        boolean doNotExists = (stats == null);
        if (doNotExists) {
            throw new RuntimeException("not found student stats");
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

    public StudentStats addExp(Long id, int exp) {
        StudentStats stats = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found student stats"));

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

    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found student"));

        StudentStats stats = student.getStats();

        student.setStats(null);
        studentRepository.save(student);

        repository.delete(stats);
    }
}
