package com.jsca.gestor_gastos_personales_api.persistence.service;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.RegisterRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.UserResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;

public interface UserService {
    UserResponse registerUser(RegisterRequest request) throws Exception;
    UserResponse getUserById(Long userId) throws Exception;
    UserResponse getUserByUsername(String username) throws Exception;
    UserEntity findUserEntityById(Long userId) throws Exception;
    UserEntity findUserEntityByUsername(String username) throws Exception;
    UserEntity findUserEntityByIdentification(String identification) throws Exception;
}
