package com.jsca.gestor_gastos_personales_api.controller;

import com.jsca.gestor_gastos_personales_api.domain.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Generar PDF de Resumen Mensual
     */
    @GetMapping("/resumen-mensual")
    public ResponseEntity<byte[]> generarResumenMensual(
            @RequestParam Integer mes,
            @RequestParam Integer anio,
            @RequestParam Long userId
    ) {
        try {
            byte[] pdfBytes = reportService.generateResumenMensualPDF(userId, anio, mes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(
                    "attachment",
                    generarNombreArchivo(mes, anio)
            );

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generar nombre del archivo PDF
     */
    private String generarNombreArchivo(Integer mes, Integer anio) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String nombreMes = (mes >= 1 && mes <= 12) ? meses[mes - 1] : "Mes";
        return String.format("resumen-%s-%d.pdf", nombreMes, anio);
    }


    @GetMapping("/transacciones-detalladas")
    public ResponseEntity<byte[]> generarTransaccionesDetalladas(
            @RequestParam Integer mes,
            @RequestParam Integer anio,
            @RequestParam Long userId
    ) {
        try {
            byte[] pdfBytes = reportService.generateTransaccionesDetalladasPDF(userId, anio, mes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(
                    "attachment",
                    generarNombreArchivo(mes, anio, "transacciones")
            );

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String generarNombreArchivo(Integer mes, Integer anio, String tipo) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String nombreMes = (mes >= 1 && mes <= 12) ? meses[mes - 1] : "Mes";
        return String.format("%s-%s-%d.pdf", tipo, nombreMes, anio);
    }
}
