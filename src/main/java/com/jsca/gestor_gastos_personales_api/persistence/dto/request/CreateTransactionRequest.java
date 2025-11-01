package com.jsca.gestor_gastos_personales_api.persistence.dto.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    @Size(max = 255)
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDate transactionDate;

    @NotNull(message = "Transaction type is required")
    private String type;

    private Long categoryId;
}