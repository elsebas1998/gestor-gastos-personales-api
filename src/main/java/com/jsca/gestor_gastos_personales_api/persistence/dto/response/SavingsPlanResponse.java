package com.jsca.gestor_gastos_personales_api.persistence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsPlanResponse {

    private Long planId;
    private String name;
    private String description;
    private BigDecimal goalAmount;
    private BigDecimal currentAmount;
    private LocalDate targetDate;
    private String planType;
    private String status;
    private Integer progressPercentage;
    private LocalDateTime createdAt;
}
