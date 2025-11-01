package com.jsca.gestor_gastos_personales_api.util.mapper;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SavingsPlanResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.SavingsPlanEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.PlanStatus;
import com.jsca.gestor_gastos_personales_api.util.emun.PlanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class SavingsPlanMapper {

    /**
     * Convierte CreateSavingsPlanRequest a SavingsPlanEntity
     */
    public SavingsPlanEntity toEntity(CreateSavingsPlanRequest request, UserEntity user) {
        if (request == null) {
            return null;
        }

        return SavingsPlanEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .goalAmount(request.getGoalAmount())
                .currentAmount(BigDecimal.ZERO)
                .targetDate(request.getTargetDate())
                .planType(PlanType.valueOf(request.getPlanType()))
                .status(PlanStatus.ACTIVE)
                .user(user)
                .build();
    }

    /**
     * Actualiza SavingsPlanEntity existente con UpdateSavingsPlanRequest
     */
    public void updateEntity(SavingsPlanEntity entity, UpdateSavingsPlanRequest request) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getGoalAmount() != null) {
            entity.setGoalAmount(request.getGoalAmount());
        }
        if (request.getTargetDate() != null) {
            entity.setTargetDate(request.getTargetDate());
        }
        if (request.getStatus() != null) {
            entity.setStatus(PlanStatus.valueOf(request.getStatus()));
        }
    }

    /**
     * Convierte SavingsPlanEntity a SavingsPlanResponse
     * Calcula el progreso automáticamente
     */
    public SavingsPlanResponse toResponse(SavingsPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        return SavingsPlanResponse.builder()
                .planId(entity.getPlanId())
                .name(entity.getName())
                .description(entity.getDescription())
                .goalAmount(entity.getGoalAmount())
                .currentAmount(entity.getCurrentAmount())
                .targetDate(entity.getTargetDate())
                .planType(entity.getPlanType().name())
                .status(entity.getStatus().name())
                .progressPercentage(entity.calculateProgress())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
