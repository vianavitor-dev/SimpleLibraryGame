package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.model.Librarian;
import com.vianavitor.simplelibrarygame.service.LibrarianService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/librarians")
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Librarian>> register(@RequestBody Librarian librarian, HttpServletRequest request) {
        Librarian saved = librarianService.register(librarian);
        ApiResponse<Librarian> response = ApiResponse.success(saved, "Librarian registered", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        Long id = librarianService.login(credentials.get("username"), credentials.get("password"));
        ApiResponse<Long> response = ApiResponse.success(id, "Login successful", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Librarian>>> getAll(HttpServletRequest request) {
        List<Librarian> librarians = librarianService.getAll();
        ApiResponse<List<Librarian>> response = ApiResponse.success(librarians, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id, HttpServletRequest request) {
        librarianService.deactivate(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Librarian deactivated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id, HttpServletRequest request) {
        librarianService.activate(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Librarian activated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}