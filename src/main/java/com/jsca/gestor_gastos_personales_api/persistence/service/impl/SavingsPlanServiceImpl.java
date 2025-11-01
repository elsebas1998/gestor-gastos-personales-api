package com.jsca.gestor_gastos_personales_api.persistence.service.impl;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SavingsPlanResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.SavingsPlanEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.persistence.repository.SavingsPlanRepository;
import com.jsca.gestor_gastos_personales_api.persistence.service.SavingsPlanService;
import com.jsca.gestor_gastos_personales_api.persistence.service.UserService;
import com.jsca.gestor_gastos_personales_api.util.emun.PlanStatus;
import com.jsca.gestor_gastos_personales_api.util.mapper.SavingsPlanMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SavingsPlanServiceImpl implements SavingsPlanService {

    private final SavingsPlanRepository savingsPlanRepository;
    private final SavingsPlanMapper savingsPlanMapper;
    private final UserService userService;

    @Override
    @Transactional
    public SavingsPlanResponse createSavingsPlan(final Long userId, final CreateSavingsPlanRequest request) throws Exception {
        UserEntity user = userService.findUserEntityById(userId);

        SavingsPlanEntity plan = savingsPlanMapper.toEntity(request, user);
        SavingsPlanEntity saved = savingsPlanRepository.save(plan);

        return savingsPlanMapper.toResponse(saved);
    }

    @Override
    public List<SavingsPlanResponse> getSavingsPlansByUser(final Long userId) throws Exception {
        List<SavingsPlanEntity> plans = savingsPlanRepository.findByUser_UserId(userId);

        return plans.stream()
                .map(savingsPlanMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SavingsPlanResponse> getActivePlans(final Long userId) throws Exception {
        List<SavingsPlanEntity> activePlans = savingsPlanRepository
                .findByUser_UserIdAndStatus(userId, PlanStatus.ACTIVE);

        return activePlans.stream()
                .map(savingsPlanMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SavingsPlanResponse getSavingsPlanById(final Long userId, final Long planId) throws Exception {
        SavingsPlanEntity plan = savingsPlanRepository.findById(planId)
                .orElseThrow(() -> new Exception("No existe el plan de ahorro"));
        if (!plan.getUser().getUserId().equals(userId)) {
            throw new Exception("El plan no pertenece al usuario");
        }

        return savingsPlanMapper.toResponse(plan);
    }

    @Override
    @Transactional
    public SavingsPlanResponse updateSavingsPlan(final Long userId, final Long planId, final UpdateSavingsPlanRequest request) throws Exception {
        SavingsPlanEntity plan = savingsPlanRepository.findById(planId)
                .orElseThrow(() -> new Exception("No existe el plan de ahorro"));
        if (!plan.getUser().getUserId().equals(userId)) {
            throw new Exception("El plan no pertenece al usuario");
        }
        savingsPlanMapper.updateEntity(plan, request);
        SavingsPlanEntity updated = savingsPlanRepository.save(plan);
        return savingsPlanMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public SavingsPlanResponse addAmountToPlan(final Long userId, final Long planId, final BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }

        SavingsPlanEntity plan = savingsPlanRepository.findById(planId)
                .orElseThrow(() -> new Exception("No existe el plan de ahorro"));

        if (!plan.getUser().getUserId().equals(userId)) {
            throw new Exception("El plan no pertenece al usuario");
        }

        if (plan.getStatus() != PlanStatus.ACTIVE) {
            throw new Exception("El plan no está activo");
        }

        plan.addAmount(amount);
        SavingsPlanEntity updated = savingsPlanRepository.save(plan);

        return savingsPlanMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public SavingsPlanResponse withdrawAmountFromPlan(final Long userId, final Long planId, final BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }

        SavingsPlanEntity plan = savingsPlanRepository.findById(planId)
                .orElseThrow(() -> new Exception("No existe el plan de ahorro"));

        if (!plan.getUser().getUserId().equals(userId)) {
            throw new Exception("El plan no pertenece al usuario");
        }

        if (plan.getCurrentAmount().compareTo(amount) < 0) {
            throw new Exception("Fondos insuficientes en el plan de ahorro");
        }

        plan.subtractAmount(amount);
        SavingsPlanEntity updated = savingsPlanRepository.save(plan);

        return savingsPlanMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSavingsPlan(final Long userId, final Long planId) throws Exception {
        SavingsPlanEntity plan = savingsPlanRepository.findById(planId)
                .orElseThrow(() -> new Exception("No existe el plan de ahorro"));

        if (!plan.getUser().getUserId().equals(userId)) {
            throw new Exception("El plan no pertenece al usuario");
        }

        savingsPlanRepository.delete(plan);
    }

    @Override
    public BigDecimal getTotalSaved(final Long userId) throws Exception {
        BigDecimal totalSaved = savingsPlanRepository.calculateTotalSaved(userId);
        return totalSaved != null ? totalSaved : BigDecimal.ZERO;
    }
}
