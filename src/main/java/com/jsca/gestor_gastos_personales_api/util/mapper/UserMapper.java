package com.jsca.gestor_gastos_personales_api.util.mapper;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.RegisterRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.UserResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre UserEntity y DTOs
 */
@Component
public class UserMapper {

    /**
     * Convierte RegisterRequest a UserEntity
     * (Para registro de nuevo usuario)
     */
    public UserEntity toEntity(final RegisterRequest request) {
        if (request == null) {
            return null;
        }

        return UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                // passwordHash se asigna en el servicio después de hashear
                .isActive(true)
                .build();
    }

    /**
     * Convierte UserEntity a UserResponse
     * (Para mostrar datos del usuario, sin password)
     */
    public UserResponse toResponse(final UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserResponse.builder()
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}