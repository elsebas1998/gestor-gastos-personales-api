package com.jsca.gestor_gastos_personales_api.controller;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.MonthlySummaryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SuccessResponse;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.TransactionResponse;
import com.jsca.gestor_gastos_personales_api.persistence.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller para gestión de transacciones
 * Endpoints: /api/transactions
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * POST /api/transactions
     * Crear nueva transacción
     *
     * Body: {
     *   "amount": 900000,
     *   "description": "Salario mensual",
     *   "transactionDate": "2025-01-15",
     *   "type": "INGRESO",
     *   "categoryId": 1
     * }
     * Response: 201 Created + TransactionResponse
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestParam final Long userId,
            @Valid @RequestBody final CreateTransactionRequest request) throws Exception {

        TransactionResponse response = transactionService.createTransaction(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/transactions?userId=1&page=0&size=10
     * Obtener transacciones con paginación
     *
     * Response: 200 OK + Page<TransactionResponse>
     */
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @RequestParam final Long userId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) throws Exception {

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<TransactionResponse> transactions = transactionService.getTransactionsByUser(userId, pageable);

        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/range?userId=1&startDate=2025-01-01&endDate=2025-01-31
     * Obtener transacciones por rango de fechas
     *
     * Response: 200 OK + List<TransactionResponse>
     */
    @GetMapping("/range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @RequestParam final Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) throws Exception {

        List<TransactionResponse> transactions = transactionService.getTransactionsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/month?userId=1&year=2025&month=1
     * Obtener transacciones de un mes específico
     *
     * Response: 200 OK + List<TransactionResponse>
     */
    @GetMapping("/month")
    public ResponseEntity<List<TransactionResponse>> getTransactionsOfMonth(
            @RequestParam final Long userId,
            @RequestParam final int year,
            @RequestParam final int month) throws Exception {

        List<TransactionResponse> transactions = transactionService.getTransactionsOfMonth(userId, year, month);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/summary?userId=1&year=2025&month=1
     * Obtener resumen mensual (para Dashboard)
     *
     * Response: 200 OK + MonthlySummaryResponse
     */
    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam final Long userId,
            @RequestParam final int year,
            @RequestParam final int month) throws Exception {

        MonthlySummaryResponse summary = transactionService.getMonthlySummary(userId, year, month);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/transactions/{transactionId}?userId=1
     * Obtener transacción específica por ID
     *
     * Response: 200 OK + TransactionResponse
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable final Long transactionId,
            @RequestParam final Long userId) throws Exception {

        TransactionResponse transaction = transactionService.getTransactionById(userId, transactionId);
        return ResponseEntity.ok(transaction);
    }

    /**
     * PUT /api/transactions/{transactionId}?userId=1
     * Actualizar transacción existente
     *
     * Body: {
     *   "amount": 355000,
     *   "description": "Alquiler + ajuste"
     * }
     * Response: 200 OK + TransactionResponse
     */
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable final Long transactionId,
            @RequestParam final Long userId,
            @Valid @RequestBody final UpdateTransactionRequest request) throws Exception {

        TransactionResponse updated = transactionService.updateTransaction(userId, transactionId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/transactions/{transactionId}?userId=1
     * Eliminar transacción
     *
     * Response: 200 OK + SuccessResponse
     */
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<SuccessResponse> deleteTransaction(
            @PathVariable final Long transactionId,
            @RequestParam final Long userId) throws Exception {

        transactionService.deleteTransaction(userId, transactionId);

        SuccessResponse response = new SuccessResponse("Transacción eliminada exitosamente");
        return ResponseEntity.ok(response);
    }
}
