package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.aux.UserInfoData;
import com.vianavitor.simplelibrarygame.model.Administrator;
import com.vianavitor.simplelibrarygame.service.AdministratorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/administrators")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Administrator>> register(@RequestBody @Valid UserInfoData request, HttpServletRequest req) {
        Administrator saved = administratorService.register(
                new Administrator(request.username(), request.password(), request.name())
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Administrator registered", req.getRequestURI()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        Long userId = administratorService.login(username, password);
        ApiResponse<Long> response = ApiResponse.success(userId, "Login successful", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}