package com.vianavitor.simplelibrarygame.controller.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.LoginRequest;
import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import com.vianavitor.simplelibrarygame.service.auth.AuthorizationService;
import com.vianavitor.simplelibrarygame.service.auth.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthorizationService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest request, HttpServletRequest req)
            throws JWTCreationException, DisabledException, BadCredentialsException {
        var login = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var auth = authenticationManager.authenticate(login);

        if (auth.getPrincipal() == null) {
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("missing principal", req.getRequestURI()));
        }

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(ApiResponse.success(token, "Login successful", req.getRequestURI()));
    }
}
