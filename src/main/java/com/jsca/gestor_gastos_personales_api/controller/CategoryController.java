package com.jsca.gestor_gastos_personales_api.controller;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.CategoryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.SuccessResponse;
import com.jsca.gestor_gastos_personales_api.persistence.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gestión de categorías
 * Endpoints: /api/categories
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * POST /api/categories
     * Crear nueva categoría
     *
     * Body: { "name": "Alquiler", "type": "EGRESO", "isFixed": true }
     * Response: 201 Created + CategoryResponse
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestParam final Long userId,
            @Valid @RequestBody final CreateCategoryRequest request) throws Exception {

        CategoryResponse response = categoryService.createCategory(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/categories?userId=1
     * Obtener todas las categorías del usuario
     *
     * Response: 200 OK + List<CategoryResponse>
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategoriesByUser(
            @RequestParam final Long userId) throws Exception {
        List<CategoryResponse> categories = categoryService.getCategoriesByUser(userId);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/categories/type?userId=1&type=EGRESO
     * Obtener categorías por tipo (INGRESO o EGRESO)
     *
     * Response: 200 OK + List<CategoryResponse>
     */
    @GetMapping("/type")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByType(
            @RequestParam final Long userId,
            @RequestParam final String type) throws Exception {

        List<CategoryResponse> categories = categoryService.getCategoriesByUserAndType(userId, type);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/categories/{categoryId}?userId=1
     * Obtener categoría específica por ID
     *
     * Response: 200 OK + CategoryResponse
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable final Long categoryId,
            @RequestParam final Long userId) throws Exception {

        CategoryResponse category = categoryService.getCategoryById(userId, categoryId);
        return ResponseEntity.ok(category);
    }

    /**
     * PUT /api/categories/{categoryId}?userId=1
     * Actualizar categoría existente
     *
     * Body: { "name": "Alquiler Actualizado", "color": "#FF0000" }
     * Response: 200 OK + CategoryResponse
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable final Long categoryId,
            @RequestParam final Long userId,
            @Valid @RequestBody final UpdateCategoryRequest request) throws Exception {

        CategoryResponse updated = categoryService.updateCategory(userId, categoryId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/categories/{categoryId}?userId=1
     * Eliminar categoría
     *
     * Response: 200 OK + SuccessResponse
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<SuccessResponse> deleteCategory(
            @PathVariable final Long categoryId,
            @RequestParam final Long userId) throws Exception {

        categoryService.deleteCategory(userId, categoryId);

        SuccessResponse response = new SuccessResponse("Categoría eliminada exitosamente");
        return ResponseEntity.ok(response);
    }
}