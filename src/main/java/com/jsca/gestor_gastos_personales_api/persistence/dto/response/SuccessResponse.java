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
public class SuccessResponse {

    private LocalDateTime timestamp;
    private String message;
    private Object data;

    public SuccessResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public SuccessResponse(String message, Object data) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }
}