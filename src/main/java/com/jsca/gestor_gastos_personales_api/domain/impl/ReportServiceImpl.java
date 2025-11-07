package com.jsca.gestor_gastos_personales_api.domain.impl;

import com.jsca.gestor_gastos_personales_api.domain.ReportService;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.TransactionReport;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.MonthlySummaryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.service.TransactionService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TransactionService transactionService;

    /**
     * Generar PDF de Resumen Mensual
     */
    @Override
    public byte[] generateResumenMensualPDF(Long userId, Integer anio, Integer mes) throws Exception {

        if (transactionService == null) {
            throw new RuntimeException("TransactionService no está disponible");
        }

        MonthlySummaryResponse summary = transactionService.getMonthlySummary(userId, anio, mes);

        String nombreMes = obtenerNombreMes(mes);
        String usuario = "Usuario";
        String tasaAhorro = formatearTasaAhorro(summary.getSavingsRate());

        return generarPDF(nombreMes, anio, usuario, summary, tasaAhorro);
    }

    private byte[] generarPDF(
            String nombreMes,
            Integer anio,
            String usuario,
            MonthlySummaryResponse summary,
            String tasaAhorro
    ) throws Exception {

        InputStream jasperStream = new ClassPathResource("reports/compiled/resumen-mensual.jasper")
                .getInputStream();

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        Map<String, Object> parameters = prepararParametros(
                nombreMes, anio, usuario, summary, tasaAhorro
        );

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                new JREmptyDataSource()
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    private Map<String, Object> prepararParametros(
            String nombreMes,
            Integer anio,
            String usuario,
            MonthlySummaryResponse summary,
            String tasaAhorro
    ) throws Exception {

        Map<String, Object> parameters = new HashMap<>();

        ClassPathResource logoResource = new ClassPathResource("reports/images/logo.png");
        ClassPathResource watermarkResource = new ClassPathResource("reports/images/watermark.png");

        String totalIngresosFormateado = formatearMoneda(summary.getTotalIncome());
        String totalEgresosFormateado = formatearMoneda(summary.getTotalExpense());
        String totalAhorroFormateado = formatearMoneda(summary.getSavings());

        parameters.put("LOGO_PATH", logoResource.getFile().getAbsolutePath());
        parameters.put("WATERMARK_PATH", watermarkResource.getFile().getAbsolutePath());
        parameters.put("MES", nombreMes);
        parameters.put("ANIO", anio);
        parameters.put("USUARIO", usuario);
        parameters.put("TOTAL_INGRESOS", totalIngresosFormateado);
        parameters.put("TOTAL_EGRESOS", totalEgresosFormateado);
        parameters.put("TOTAL_AHORRO", totalAhorroFormateado);
        parameters.put("TASA_AHORRO", tasaAhorro);

        return parameters;
    }


    private String formatearMoneda(BigDecimal monto) {
        if (monto == null) {
            return "$0.00";
        }
        return String.format("$%,.2f", monto);
    }

    private String obtenerNombreMes(Integer mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes inválido: " + mes);
        }

        return meses[mes - 1];
    }

    private String formatearTasaAhorro(BigDecimal tasaDecimal) {
        if (tasaDecimal == null) {
            return "0.0%";
        }
        return String.format("%.1f%%", tasaDecimal);
    }

    /**
     * Generar PDF de Transacciones Detalladas
     */
    public byte[] generateTransaccionesDetalladasPDF(Long userId, Integer anio, Integer mes) throws Exception {

        if (transactionService == null) {
            throw new RuntimeException("TransactionService no está disponible");
        }

        MonthlySummaryResponse summary = transactionService.getMonthlySummary(userId, anio, mes);

        List<TransactionReport> transactions = transactionService.getTransactionsByMonthAndYear(userId, anio, mes);

        String nombreMes = obtenerNombreMes(mes);
        String usuario = "Usuario";

        return generarPDFTransacciones(nombreMes, anio, usuario, summary, transactions);
    }

    private byte[] generarPDFTransacciones(
            String nombreMes,
            Integer anio,
            String usuario,
            MonthlySummaryResponse summary,
            List<TransactionReport> transactions
    ) throws Exception {

        InputStream jasperStream = new ClassPathResource("reports/compiled/transacciones-detalladas.jasper")
                .getInputStream();

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        Map<String, Object> parameters = new HashMap<>();

        ClassPathResource logoResource = new ClassPathResource("reports/images/logo.png");
        ClassPathResource watermarkResource = new ClassPathResource("reports/images/watermark.png");

        parameters.put("LOGO_PATH", logoResource.getFile().getAbsolutePath());
        parameters.put("WATERMARK_PATH", watermarkResource.getFile().getAbsolutePath());
        parameters.put("MES", nombreMes);
        parameters.put("ANIO", anio);
        parameters.put("USUARIO", usuario);
        parameters.put("TOTAL_TRANSACCIONES", transactions.size());
        parameters.put("TOTAL_INGRESOS", formatearMoneda(summary.getTotalIncome()));
        parameters.put("TOTAL_EGRESOS", formatearMoneda(summary.getTotalExpense()));

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactions);

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}


