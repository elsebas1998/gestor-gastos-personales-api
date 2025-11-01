package com.jsca.gestor_gastos_personales_api.persistence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long categoryId;
    private String name;
    private String type;
    private Boolean isFixed;
    private String icon;
    private String color;
    private String description;
    private LocalDateTime createdAt;
}