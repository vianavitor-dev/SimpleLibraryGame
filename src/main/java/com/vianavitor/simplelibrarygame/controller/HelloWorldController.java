package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class HelloWorldController {
    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<Void>> helloWorld() {
        ApiResponse<Void> response = ApiResponse.success(null, "Hello World!", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Void>> getMessage() {
        ApiResponse<Void> response = ApiResponse.success(null, "Message example", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
