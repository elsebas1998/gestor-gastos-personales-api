package com.jsca.gestor_gastos_personales_api.controller;

import com.jsca.gestor_gastos_personales_api.persistence.entities.FinancialTipEntity;
import com.jsca.gestor_gastos_personales_api.persistence.service.FinancialTipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para consejos financieros
 * Endpoints: /api/tips
 */
@RestController
@RequestMapping("/tips")
@RequiredArgsConstructor
public class FinancialTipController {

    private final FinancialTipService financialTipService;

    /**
     * GET /api/tips/random
     * Obtener un consejo aleatorio (para mostrar al abrir la app)
     *
     * Response: 200 OK + FinancialTipEntity
     */
    @GetMapping("/random")
    public ResponseEntity<FinancialTipEntity> getRandomTip() throws Exception {
        FinancialTipEntity tip = financialTipService.getRandomTip();
        return ResponseEntity.ok(tip);
    }

    /**
     * GET /api/tips/random-multiple?limit=3
     * Obtener N consejos aleatorios
     *
     * Response: 200 OK + List<FinancialTipEntity>
     */
    @GetMapping("/random-multiple")
    public ResponseEntity<List<FinancialTipEntity>> getRandomTips(
            @RequestParam(defaultValue = "3") final int limit) throws Exception {

        List<FinancialTipEntity> tips = financialTipService.getRandomTips(limit);
        return ResponseEntity.ok(tips);
    }

    /**
     * GET /api/tips/random-by-type?type=AHORRO
     * Obtener consejo aleatorio de un tipo específico
     *
     * Response: 200 OK + FinancialTipEntity
     */
    @GetMapping("/random-by-type")
    public ResponseEntity<FinancialTipEntity> getRandomTipByType(
            @RequestParam final String type) throws Exception {

        FinancialTipEntity tip = financialTipService.getRandomTipByType(type);
        return ResponseEntity.ok(tip);
    }

    /**
     * GET /api/tips
     * Obtener todos los consejos activos
     *
     * Response: 200 OK + List<FinancialTipEntity>
     */
    @GetMapping
    public ResponseEntity<List<FinancialTipEntity>> getAllActiveTips() throws Exception {
        List<FinancialTipEntity> tips = financialTipService.getAllActiveTips();
        return ResponseEntity.ok(tips);
    }
}