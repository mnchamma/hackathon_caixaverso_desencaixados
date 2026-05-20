package com.acessibilidade.api.controller;

import com.acessibilidade.api.dto.LoginRequest;
import com.acessibilidade.api.dto.LoginResponse;
import com.acessibilidade.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }
}
