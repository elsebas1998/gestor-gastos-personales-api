package com.jsca.gestor_gastos_personales_api.controller;

import com.jsca.gestor_gastos_personales_api.domain.CoreServices;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.LoginRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.RegisterRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.UserResponse;
import com.jsca.gestor_gastos_personales_api.persistence.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para autenticación y registro de usuarios
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CoreServices coreServices;

    /**
     * Registrar nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody final RegisterRequest request) throws Exception {
        UserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Iniciar sesión
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody final LoginRequest request) throws Exception {
        return coreServices.login(request);
    }
}