package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.AddExperienceRequest;
import com.vianavitor.simplelibrarygame.dto.request.AverageReadingTimeRequest;
import com.vianavitor.simplelibrarygame.dto.request.SetCurrentBookRequest;
import com.vianavitor.simplelibrarygame.dto.response.StudentStatsResponse;
import com.vianavitor.simplelibrarygame.dto.response.UsersBookReadHistoryResponse;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.service.StudentStatsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stats")
public class StudentStatsController {

    @Autowired
    private StudentStatsService statsService;

    private StudentStatsResponse studentStatsToDto(StudentStats stats) {
        BookReadHistory history = stats.getCurrentBook();
        UsersBookReadHistoryResponse userHistory = null;

        if (history != null) {
            Book book = history.getBook();

            userHistory = new UsersBookReadHistoryResponse(
                    history.getId(),
                    new UsersBookReadHistoryResponse.BookInfo(
                            book.getId(),
                            book.getTitle()
                    ),
                    history.getLastUpdate(),
                    history.getLastPageRead()
            );
        }

        return new StudentStatsResponse(
                stats.getId(),
                userHistory,
                stats.getLevel(),
                stats.getCurrentExperience(),
                stats.getMaxLvlExperience(),
                stats.getOngoingStreak(),
                stats.getAverageReadingTime(),
                stats.getReadingCount()
        );
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<ApiResponse<StudentStatsResponse>> createStats(@PathVariable Long userId, HttpServletRequest request) {
        StudentStats stats = statsService.create(userId);
        ApiResponse<StudentStatsResponse> response = ApiResponse.success(this.studentStatsToDto(stats), "Stats created", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<StudentStatsResponse>> getStats(@PathVariable Long userId, HttpServletRequest request) {
        // TODO: this is getting a loop in the JSON, fix it
        StudentStats stats = statsService.get(userId);
        ApiResponse<StudentStatsResponse> response = ApiResponse.success(this.studentStatsToDto(stats), request.getRequestURI());
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
    public ResponseEntity<ApiResponse<StudentStatsResponse>> addExperience(
            @PathVariable Long statsId,
            @Valid @RequestBody AddExperienceRequest request,
            HttpServletRequest req
    ) {
        StudentStatsResponse stats = this.studentStatsToDto(
                statsService.addExp(statsId, request.exp())
        );
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