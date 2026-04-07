package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.service.InvalidTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invalid-tokens")
public class InvalidTokenController {

    @Autowired
    private InvalidTokenService invalidTokenService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addToken(@RequestParam String token, HttpServletRequest request) {
        invalidTokenService.add(token);
        ApiResponse<Void> response = ApiResponse.success(null, "Token invalidated", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> isTokenValid(@RequestParam String token, HttpServletRequest request) {
        boolean valid = invalidTokenService.isTokenValid(token);
        ApiResponse<Boolean> response = ApiResponse.success(valid, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}