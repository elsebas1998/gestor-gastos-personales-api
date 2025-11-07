package com.jsca.gestor_gastos_personales_api.domain;


import com.jsca.gestor_gastos_personales_api.persistence.dto.response.MonthlySummaryResponse;

public interface ReportService {

    public byte[] generateResumenMensualPDF(Long userId, Integer anio, Integer mes) throws Exception;

    public byte[] generateTransaccionesDetalladasPDF(Long userId, Integer anio, Integer mes) throws Exception;
}
