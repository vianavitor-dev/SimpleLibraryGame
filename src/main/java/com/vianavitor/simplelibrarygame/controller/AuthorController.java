package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.AddAuthorRequest;
import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.service.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addAuthor(@Valid @RequestBody AddAuthorRequest request, HttpServletRequest req) {
        authorService.add(request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Author added", req.getRequestURI()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Author>> getByName(@PathVariable String name, HttpServletRequest request) {
        Author author = authorService.getByName(name);
        ApiResponse<Author> response = ApiResponse.success(author, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Author>>> getAll(HttpServletRequest request) {
        List<Author> authors = authorService.getAll();
        ApiResponse<List<Author>> response = ApiResponse.success(authors, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Author>> getById(@PathVariable Long id, HttpServletRequest request) {
        Author author = authorService.get(id);
        ApiResponse<Author> response = ApiResponse.success(author, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<ApiResponse<List<Book>>> getAuthorBooks(@PathVariable Long id, HttpServletRequest request) {
        List<Book> books = authorService.getAuthorBooks(id);
        ApiResponse<List<Book>> response = ApiResponse.success(books, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}