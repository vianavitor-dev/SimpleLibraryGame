package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.LoginRequest;
import com.vianavitor.simplelibrarygame.dto.request.RegisterStudentRequest;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Student>> register(@Valid @RequestBody RegisterStudentRequest request, HttpServletRequest req) {
        studentService.register(request.student(), request.classroomCode(), request.favoriteGenres());
        Student saved = request.student();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Student registered", req.getRequestURI()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest req) {
        Long id = studentService.login(request.username(), request.password());
        return ResponseEntity.ok(ApiResponse.success(id, "Login successful", req.getRequestURI()));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<StudentStats>> getStats(@PathVariable Long id, HttpServletRequest request) {
        StudentStats stats = studentService.getStats(id);
        ApiResponse<StudentStats> response = ApiResponse.success(stats, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAll(HttpServletRequest request) {
        List<Student> students = studentService.getAll();
        ApiResponse<List<Student>> response = ApiResponse.success(students, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<ApiResponse<Set<Genre>>> getFavoriteGenres(@PathVariable Long id, HttpServletRequest request) {
        Set<Genre> favorites = studentService.getFavoriteGenres(id);
        ApiResponse<Set<Genre>> response = ApiResponse.success(favorites, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id, HttpServletRequest request) {
        studentService.deactivate(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Student deactivated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id, HttpServletRequest request) {
        studentService.activate(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Student activated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}