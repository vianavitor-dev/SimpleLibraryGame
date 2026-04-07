package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.LoginRequest;
import com.vianavitor.simplelibrarygame.dto.request.RegisterProfessorRequest;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.service.ProfessorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Professor>> register(@Valid @RequestBody RegisterProfessorRequest request, HttpServletRequest req) {
        professorService.register(request.professor(), request.classroomCode());
        Professor saved = request.professor();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Professor registered", req.getRequestURI()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest req) {
        Long id = professorService.login(request.username(), request.password());
        return ResponseEntity.ok(ApiResponse.success(id, "Login successful", req.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Professor>>> getAll(HttpServletRequest request) {
        List<Professor> professors = professorService.getAll();
        ApiResponse<List<Professor>> response = ApiResponse.success(professors, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id, HttpServletRequest request) {
        professorService.deactivate(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Professor deactivated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id, HttpServletRequest request) {
        professorService.activate(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Professor activated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}