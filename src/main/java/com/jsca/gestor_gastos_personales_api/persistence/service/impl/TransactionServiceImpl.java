package com.jsca.gestor_gastos_personales_api.persistence.service.impl;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.MonthlySummaryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.TransactionResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.CategoryEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.TransactionEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.persistence.repository.TransactionRepository;
import com.jsca.gestor_gastos_personales_api.persistence.service.CategoryService;
import com.jsca.gestor_gastos_personales_api.persistence.service.TransaccionService;
import com.jsca.gestor_gastos_personales_api.persistence.service.UserService;
import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import com.jsca.gestor_gastos_personales_api.util.mapper.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de transacciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransaccionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    /**
     * Crear nueva transacción
     */
    @Override
    @Transactional
    public TransactionResponse createTransaction(final Long userId, final CreateTransactionRequest request) throws Exception {
        UserEntity user = userService.findUserEntityById(userId);
        CategoryEntity category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.findCategoryEntityById(request.getCategoryId());
            if (!category.getUser().getUserId().equals(userId)) {
                throw new Exception("Categoria invalida para el usuario");
            }
            if (!category.getType().name().equals(request.getType())) {
                throw new Exception(
                        String.format("Categoria no correspondiente",
                                category.getType(), request.getType())
                );
            }
        }
        TransactionEntity transaction = transactionMapper.toEntity(request, user, category);
        TransactionEntity saved = transactionRepository.save(transaction);
        return transactionMapper.toResponse(saved);
    }

    /**
     * Obtener transacciones paginadas
     */
    @Override
    public Page<TransactionResponse> getTransactionsByUser(final Long userId, final Pageable pageable) {
        Page<TransactionEntity> transactions = transactionRepository.findByUser_UserId(userId, pageable);

        return transactions.map(transactionMapper::toResponse);
    }

    /**
     * Obtener transacciones por rango de fechas
     */
    @Override
    public List<TransactionResponse> getTransactionsByDateRange(
            final Long userId,
            final LocalDate startDate,
            final LocalDate endDate) {
        List<TransactionEntity> transactions = transactionRepository
                .findByUser_UserIdAndTransactionDateBetween(userId, startDate, endDate);
        return transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener transacciones del mes actual
     */
    @Override
    public List<TransactionResponse> getTransactionsOfMonth(Long userId, int year, int month) {
        List<TransactionEntity> transactions = transactionRepository.findByUserAndMonth(userId, year, month);
        return transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener resumen mensual (para Dashboard)
     */
    @Override
    public MonthlySummaryResponse getMonthlySummary(final Long userId, int year, int month) {
        // Calcular totales por tipo
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        List<TransactionEntity> transactions = transactionRepository
                .findByUser_UserIdAndTransactionDateBetween(userId, startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INGRESO)
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EGRESO)
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal savings = totalIncome.subtract(totalExpense);

        BigDecimal savingsRate = BigDecimal.ZERO;
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = savings.divide(totalIncome, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return MonthlySummaryResponse.builder()
                .year(year)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .savings(savings)
                .savingsRate(savingsRate)
                .build();
    }

    /**
     * Obtener transacción por ID
     */
    @Override
    public TransactionResponse getTransactionById(final Long userId, final Long transactionId) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Erro al obtener la transaccion"));
        if (!transaction.getUser().getUserId().equals(userId)) {
            throw new Exception("Erro al obtener la transaccion");
        }
        return transactionMapper.toResponse(transaction);
    }

    /**
     * Actualizar transacción
     */
    @Override
    @Transactional
    public TransactionResponse updateTransaction(
            final Long userId,
            final Long transactionId,
            final UpdateTransactionRequest request) throws Exception {

        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Error al actualizar transaccion"));

        if (!transaction.getUser().getUserId().equals(userId)) {
            throw new Exception("Error al actualizar transaccion");
        }
        CategoryEntity newCategory = null;
        if (request.getCategoryId() != null) {
            newCategory = categoryService.findCategoryEntityById(request.getCategoryId());

            if (!newCategory.getUser().getUserId().equals(userId)) {
                throw new Exception("Categoria no es del usuario");
            }
            if (!newCategory.getType().equals(transaction.getType())) {
                throw new Exception("Categoria incorrecta");
            }
        }
        transactionMapper.updateEntity(transaction, request, newCategory);
        TransactionEntity updated = transactionRepository.save(transaction);
        return transactionMapper.toResponse(updated);
    }

    /**
     * Eliminar transacción
     */
    @Override
    @Transactional
    public void deleteTransaction(final Long userId, final Long transactionId) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Transaction no valida"));
        if (!transaction.getUser().getUserId().equals(userId)) {
            throw new Exception("Transaction no valida");
        }
        transactionRepository.delete(transaction);
    }
}
