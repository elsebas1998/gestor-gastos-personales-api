package com.jsca.gestor_gastos_personales_api.persistence.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {

    @Size(max = 100)
    private String name;

    private Boolean isFixed;

    @Size(max = 50)
    private String icon;

    @Size(max = 20)
    private String color;

    private String description;
}
