package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.service.GenreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Genre>>> getAll(HttpServletRequest request) {
        List<Genre> genres = genreService.getAll();
        ApiResponse<List<Genre>> response = ApiResponse.success(genres, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Genre>> getById(@PathVariable Long id, HttpServletRequest request) {
        Genre genre = genreService.getById(id);
        ApiResponse<Genre> response = ApiResponse.success(genre, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Genre>> getByName(@PathVariable String name, HttpServletRequest request) {
        Genre genre = genreService.getByName(name);
        ApiResponse<Genre> response = ApiResponse.success(genre, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}