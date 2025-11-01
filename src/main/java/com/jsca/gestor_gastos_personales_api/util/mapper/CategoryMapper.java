package com.jsca.gestor_gastos_personales_api.util.mapper;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.CategoryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.CategoryEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre CategoryEntity y DTOs
 */
@Component
public class CategoryMapper {

    /**
     * Convierte CreateCategoryRequest a CategoryEntity
     */
    public CategoryEntity toEntity(CreateCategoryRequest request, UserEntity user) {
        if (request == null) {
            return null;
        }

        return CategoryEntity.builder()
                .name(request.getName())
                .type(TransactionType.valueOf(request.getType()))
                .isFixed(request.getIsFixed() != null ? request.getIsFixed() : false)
                .icon(request.getIcon())
                .color(request.getColor())
                .description(request.getDescription())
                .user(user)
                .build();
    }

    /**
     * Actualiza CategoryEntity existente con UpdateCategoryRequest
     */
    public void updateEntity(CategoryEntity entity, UpdateCategoryRequest request) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getIsFixed() != null) {
            entity.setIsFixed(request.getIsFixed());
        }
        if (request.getIcon() != null) {
            entity.setIcon(request.getIcon());
        }
        if (request.getColor() != null) {
            entity.setColor(request.getColor());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }

    /**
     * Convierte CategoryEntity a CategoryResponse
     */
    public CategoryResponse toResponse(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return CategoryResponse.builder()
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .type(entity.getType().name())
                .isFixed(entity.getIsFixed())
                .icon(entity.getIcon())
                .color(entity.getColor())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}