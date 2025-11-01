package com.jsca.gestor_gastos_personales_api.persistence.service;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.CategoryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.CategoryEntity;

import java.util.List;

public interface CategoryService {


    CategoryResponse createCategory(Long userId, CreateCategoryRequest request) throws Exception;

    List<CategoryResponse> getCategoriesByUser(Long userId);

    List<CategoryResponse> getCategoriesByUserAndType(Long userId, String type);

    CategoryResponse getCategoryById(Long userId, Long categoryId) throws Exception;

    CategoryResponse updateCategory(Long userId, Long categoryId, UpdateCategoryRequest request) throws Exception;

    void deleteCategory(Long userId, Long categoryId) throws Exception;

    CategoryEntity findCategoryEntityById(Long categoryId) throws Exception;
}
