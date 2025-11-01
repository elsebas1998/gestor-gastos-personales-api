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
public class UpdateSavingsPlanRequest {

    @Size(max = 100)
    private String name;

    private String description;

    @DecimalMin(value = "0.01")
    private BigDecimal goalAmount;

    @Future
    private LocalDate targetDate;

    private String status;
}
