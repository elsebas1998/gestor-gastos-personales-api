package com.jsca.gestor_gastos_personales_api.domain.impl;

import com.jsca.gestor_gastos_personales_api.domain.CoreServices;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.LoginRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.UserResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.persistence.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreServicesImpl implements CoreServices {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<UserResponse> login(LoginRequest request) throws Exception {
       UserEntity user = userService.findUserEntityByUsername(request.getUsername());
       if(passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
           throw new Exception("Contraseña incorrecta");
       }
       UserResponse userResponse = userService.getUserByUsername(request.getUsername());
       return ResponseEntity.ok(userResponse);

    }
}
