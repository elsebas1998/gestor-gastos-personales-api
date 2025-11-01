package com.jsca.gestor_gastos_personales_api.util.mapper;


import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.TransactionResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.CategoryEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.TransactionEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    /**
     * Convierte CreateTransactionRequest a TransactionEntity
     */
    public TransactionEntity toEntity(CreateTransactionRequest request,
                                      UserEntity user,
                                      CategoryEntity category) {
        if (request == null) {
            return null;
        }

        return TransactionEntity.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .type(TransactionType.valueOf(request.getType()))
                .user(user)
                .category(category)
                .build();
    }

    /**
     * Actualiza TransactionEntity existente con UpdateTransactionRequest
     */
    public void updateEntity(TransactionEntity entity,
                             UpdateTransactionRequest request,
                             CategoryEntity category) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getAmount() != null) {
            entity.setAmount(request.getAmount());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getTransactionDate() != null) {
            entity.setTransactionDate(request.getTransactionDate());
        }
        if (request.getCategoryId() != null) {
            entity.setCategory(category);
        }
    }

    /**
     * Convierte TransactionEntity a TransactionResponse
     * Incluye información de categoría embebida
     */
    public TransactionResponse toResponse(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        TransactionResponse response = TransactionResponse.builder()
                .transactionId(entity.getTransactionId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .transactionDate(entity.getTransactionDate())
                .type(entity.getType().name())
                .createdAt(entity.getCreatedAt())
                .build();

        if (entity.getCategory() != null) {
            response.setCategoryId(entity.getCategory().getCategoryId());
            response.setCategoryName(entity.getCategory().getName());
            response.setCategoryIcon(entity.getCategory().getIcon());
        }

        return response;
    }
}