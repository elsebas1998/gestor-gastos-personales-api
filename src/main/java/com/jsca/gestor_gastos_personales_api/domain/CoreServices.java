package com.jsca.gestor_gastos_personales_api.domain;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.LoginRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface CoreServices {
    ResponseEntity<UserResponse> login(LoginRequest request) throws Exception;
}
