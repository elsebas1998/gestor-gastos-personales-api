package com.jsca.gestor_gastos_personales_api.persistence.service;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateSavingsPlanRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SavingsPlanResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SavingsPlanService {

    SavingsPlanResponse createSavingsPlan(Long userId, CreateSavingsPlanRequest request) throws Exception;


    List<SavingsPlanResponse> getSavingsPlansByUser(Long userId) throws Exception;


    List<SavingsPlanResponse> getActivePlans(Long userId) throws Exception;


    SavingsPlanResponse getSavingsPlanById(Long userId, Long planId) throws Exception;


    SavingsPlanResponse updateSavingsPlan(Long userId, Long planId, UpdateSavingsPlanRequest request) throws Exception;


    SavingsPlanResponse addAmountToPlan(Long userId, Long planId, BigDecimal amount) throws Exception;


    SavingsPlanResponse withdrawAmountFromPlan(Long userId, Long planId, BigDecimal amount) throws Exception;


    void deleteSavingsPlan(Long userId, Long planId) throws Exception;


    BigDecimal getTotalSaved(Long userId) throws Exception;
}
