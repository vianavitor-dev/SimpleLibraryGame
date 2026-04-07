package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.AddExperienceRequest;
import com.vianavitor.simplelibrarygame.dto.request.AverageReadingTimeRequest;
import com.vianavitor.simplelibrarygame.dto.request.SetCurrentBookRequest;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.service.StudentStatsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StudentStatsController {

    @Autowired
    private StudentStatsService statsService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<ApiResponse<StudentStats>> createStats(@PathVariable Long userId, HttpServletRequest request) {
        StudentStats stats = statsService.create(userId);
        ApiResponse<StudentStats> response = ApiResponse.success(stats, "Stats created", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<StudentStats>> getStats(@PathVariable Long userId, HttpServletRequest request) {
        StudentStats stats = statsService.get(userId);
        ApiResponse<StudentStats> response = ApiResponse.success(stats, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{statsId}/average-reading-time")
    public ResponseEntity<ApiResponse<Integer>> calculateAverageReadingTime(
            @PathVariable Long statsId,
            @Valid @RequestBody AverageReadingTimeRequest request,
            HttpServletRequest req
    ) {
        int avg = statsService.calculateAverageReadingTime(statsId, request.readingTimeInMins());
        return ResponseEntity.ok(ApiResponse.success(avg, "Average updated", req.getRequestURI()));
    }

    @PostMapping("/{statsId}/add-exp")
    public ResponseEntity<ApiResponse<StudentStats>> addExperience(
            @PathVariable Long statsId,
            @Valid @RequestBody AddExperienceRequest request,
            HttpServletRequest req
    ) {
        StudentStats stats = statsService.addExp(statsId, request.exp());
        return ResponseEntity.ok(ApiResponse.success(stats, "Experience added", req.getRequestURI()));
    }

    @PostMapping("/set-current-book")
    public ResponseEntity<ApiResponse<Void>> setCurrentBook(
            @Valid @RequestBody SetCurrentBookRequest request,
            HttpServletRequest req
    ) {
        statsService.setCurrentBook(request.userId(), request.bookId(), request.page());
        return ResponseEntity.ok(ApiResponse.success(null, "Current book set", req.getRequestURI()));
    }

    @PostMapping("/{userId}/streak")
    public ResponseEntity<ApiResponse<Integer>> updateStreak(@PathVariable Long userId, HttpServletRequest request) {
        int streak = statsService.setOngoingSteak(userId);
        ApiResponse<Integer> response = ApiResponse.success(streak, "Streak updated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteStats(@PathVariable Long userId, HttpServletRequest request) {
        statsService.delete(userId);
        ApiResponse<Void> response = ApiResponse.success(null, "Stats deleted", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}