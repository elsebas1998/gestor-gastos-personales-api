package com.jsca.gestor_gastos_personales_api.controller;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SavingsPlanResponse;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SuccessResponse;
import com.jsca.gestor_gastos_personales_api.persistence.service.SavingsPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller para gestión de planes de ahorro
 * Endpoints: /api/savings-plans
 */
@RestController
@RequestMapping("/savings-plans")
@RequiredArgsConstructor
public class SavingsPlanController {

    private final SavingsPlanService savingsPlanService;

    /**
     * POST /api/savings-plans
     * Crear nuevo plan de ahorro
     *
     * Body: {
     *   "name": "Fondo de Emergencia",
     *   "goalAmount": 2580000,
     *   "targetDate": "2026-01-31",
     *   "planType": "EMERGENCY_FUND"
     * }
     * Response: 201 Created + SavingsPlanResponse
     */
    @PostMapping
    public ResponseEntity<SavingsPlanResponse> createSavingsPlan(
            @RequestParam final Long userId,
            @Valid @RequestBody final CreateSavingsPlanRequest request) throws Exception {

        SavingsPlanResponse response = savingsPlanService.createSavingsPlan(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/savings-plans?userId=1
     * Obtener todos los planes del usuario
     *
     * Response: 200 OK + List<SavingsPlanResponse>
     */
    @GetMapping
    public ResponseEntity<List<SavingsPlanResponse>> getSavingsPlans(
            @RequestParam final Long userId) throws Exception {

        List<SavingsPlanResponse> plans = savingsPlanService.getSavingsPlansByUser(userId);
        return ResponseEntity.ok(plans);
    }

    /**
     * GET /api/savings-plans/active?userId=1
     * Obtener solo planes activos
     *
     * Response: 200 OK + List<SavingsPlanResponse>
     */
    @GetMapping("/active")
    public ResponseEntity<List<SavingsPlanResponse>> getActivePlans(
            @RequestParam final Long userId) throws Exception {

        List<SavingsPlanResponse> activePlans = savingsPlanService.getActivePlans(userId);
        return ResponseEntity.ok(activePlans);
    }

    /**
     * GET /api/savings-plans/{planId}?userId=1
     * Obtener plan específico por ID
     *
     * Response: 200 OK + SavingsPlanResponse
     */
    @GetMapping("/{planId}")
    public ResponseEntity<SavingsPlanResponse> getSavingsPlanById(
            @PathVariable final Long planId,
            @RequestParam final Long userId) throws Exception {

        SavingsPlanResponse plan = savingsPlanService.getSavingsPlanById(userId, planId);
        return ResponseEntity.ok(plan);
    }

    /**
     * PUT /api/savings-plans/{planId}?userId=1
     * Actualizar plan existente
     *
     * Body: {
     *   "name": "Fondo de Emergencia Actualizado",
     *   "goalAmount": 3000000
     * }
     * Response: 200 OK + SavingsPlanResponse
     */
    @PutMapping("/{planId}")
    public ResponseEntity<SavingsPlanResponse> updateSavingsPlan(
            @PathVariable final Long planId,
            @RequestParam final Long userId,
            @Valid @RequestBody final UpdateSavingsPlanRequest request) throws Exception {

        SavingsPlanResponse updated = savingsPlanService.updateSavingsPlan(userId, planId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * PATCH /api/savings-plans/{planId}/add?userId=1&amount=50000
     * Agregar monto al plan de ahorro
     *
     * Response: 200 OK + SavingsPlanResponse
     */
    @PatchMapping("/{planId}/add")
    public ResponseEntity<SavingsPlanResponse> addAmountToPlan(
            @PathVariable final Long planId,
            @RequestParam final Long userId,
            @RequestParam final BigDecimal amount) throws Exception {

        SavingsPlanResponse updated = savingsPlanService.addAmountToPlan(userId, planId, amount);
        return ResponseEntity.ok(updated);
    }

    /**
     * PATCH /api/savings-plans/{planId}/withdraw?userId=1&amount=10000
     * Retirar monto del plan de ahorro
     *
     * Response: 200 OK + SavingsPlanResponse
     */
    @PatchMapping("/{planId}/withdraw")
    public ResponseEntity<SavingsPlanResponse> withdrawAmountFromPlan(
            @PathVariable final Long planId,
            @RequestParam final Long userId,
            @RequestParam final BigDecimal amount) throws Exception {

        SavingsPlanResponse updated = savingsPlanService.withdrawAmountFromPlan(userId, planId, amount);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /api/savings-plans/total-saved?userId=1
     * Obtener total ahorrado por el usuario
     *
     * Response: 200 OK + BigDecimal
     */
    @GetMapping("/total-saved")
    public ResponseEntity<BigDecimal> getTotalSaved(
            @RequestParam final Long userId) throws Exception {

        BigDecimal totalSaved = savingsPlanService.getTotalSaved(userId);
        return ResponseEntity.ok(totalSaved);
    }

    /**
     * DELETE /api/savings-plans/{planId}?userId=1
     * Eliminar plan de ahorro
     *
     * Response: 200 OK + SuccessResponse
     */
    @DeleteMapping("/{planId}")
    public ResponseEntity<SuccessResponse> deleteSavingsPlan(
            @PathVariable final Long planId,
            @RequestParam final Long userId) throws Exception {

        savingsPlanService.deleteSavingsPlan(userId, planId);

        SuccessResponse response = new SuccessResponse("Plan de ahorro eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
}
