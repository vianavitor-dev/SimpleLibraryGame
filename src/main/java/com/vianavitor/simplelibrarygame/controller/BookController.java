package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.AddBookRequest;
import com.vianavitor.simplelibrarygame.dto.request.RateBookRequest;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addBook(@Valid @RequestBody AddBookRequest request, HttpServletRequest req) {
        bookService.add(request.book(), request.confirmed());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Book added", req.getRequestURI()));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<ApiResponse<Void>> rateBook(
            @PathVariable Long id,
            @Valid @RequestBody RateBookRequest request,
            HttpServletRequest req
    ) {
        bookService.rate(id, request.rate());
        return ResponseEntity.ok(ApiResponse.success(null, "Rating submitted", req.getRequestURI()));
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<ApiResponse<Book>> changeImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        Book updated = bookService.changeImage(id, file);
        ApiResponse<Book> response = ApiResponse.success(updated, "Image updated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<ApiResponse<Void>> setAvailable(
            @PathVariable Long id,
            @RequestParam boolean available,
            HttpServletRequest request
    ) {
        bookService.setAvailable(id, available);
        ApiResponse<Void> response = ApiResponse.success(null, "Availability updated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> modifyBook(
            @PathVariable Long id,
            @RequestBody Book data,
            @RequestParam(required = false, defaultValue = "false") boolean confirmed,
            HttpServletRequest request
    ) {
        // TODO: implement a service-level cache
        Book updated = bookService.modify(id, data, confirmed, new java.util.HashMap<>());
        ApiResponse<Book> response = ApiResponse.success(updated, "Book modified", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}