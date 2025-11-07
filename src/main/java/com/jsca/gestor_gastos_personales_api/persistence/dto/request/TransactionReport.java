package com.jsca.gestor_gastos_personales_api.persistence.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReport {
    private Long transactionId;
    private String description;
    private String categoryName;
    private BigDecimal amount;
    private String type; 
    private LocalDate transactionDate;
}
