package com.jsca.gestor_gastos_personales_api.persistence.service;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateTransactionRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.MonthlySummaryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {


    TransactionResponse createTransaction(Long userId, CreateTransactionRequest request) throws Exception;

    Page<TransactionResponse> getTransactionsByUser(Long userId, Pageable pageable);

    List<TransactionResponse> getTransactionsOfMonth(Long userId, int year, int month);


    MonthlySummaryResponse getMonthlySummary(Long userId, int year, int month);


    TransactionResponse getTransactionById(Long userId, Long transactionId) throws Exception;

    TransactionResponse updateTransaction(Long userId, Long transactionId, UpdateTransactionRequest request) throws Exception;

    void deleteTransaction(Long userId, Long transactionId) throws Exception;

    public List<TransactionResponse> getTransactionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
