package com.jsca.gestor_gastos_personales_api.persistence.service.impl;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.CreateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.request.UpdateCategoryRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.CategoryResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.CategoryEntity;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.persistence.repository.CategoryRepository;
import com.jsca.gestor_gastos_personales_api.persistence.service.CategoryService;
import com.jsca.gestor_gastos_personales_api.persistence.service.UserService;
import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import com.jsca.gestor_gastos_personales_api.util.mapper.CategoryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    /**
     * Crear nueva categoría
     */
    public CategoryResponse createCategory(final Long userId, final CreateCategoryRequest request) throws Exception {
        UserEntity user = userService.findUserEntityById(userId);
        TransactionType type = TransactionType.valueOf(request.getType());
        if (categoryRepository.existsByUser_UserIdAndNameAndType(userId, request.getName(), type)) {
            throw new Exception(
                    String.format("Category ya existe",
                            request.getName(), request.getType())
            );
        }
        CategoryEntity category = categoryMapper.toEntity(request, user);
        CategoryEntity saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    /**
     * Obtener todas las categorías del usuario
     */
    public List<CategoryResponse> getCategoriesByUser(final Long userId) {
        List<CategoryEntity> categories = categoryRepository.findByUser_UserId(userId);
        return categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener categorías por tipo
     */
    public List<CategoryResponse> getCategoriesByUserAndType(final Long userId, final String type) {
        TransactionType transactionType = TransactionType.valueOf(type);
        List<CategoryEntity> categories = categoryRepository.findByUser_UserIdAndType(userId, transactionType);
        return categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }
    

    /**
     * Obtener categoría por ID
     */
    public CategoryResponse getCategoryById(final Long userId, final Long categoryId) throws Exception {
        try {
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new Exception("Error en datos"));
            if (!category.getUser().getUserId().equals(userId)) {
                throw new Exception("");
            }
            return categoryMapper.toResponse(category);
        } catch (Exception ex) {
            throw new Exception("Ha ocurrido un error al obtener la categoria", ex);
        }

    }

    /**
     * Actualizar categoría
     */
    public CategoryResponse updateCategory(final Long userId, final Long categoryId, final UpdateCategoryRequest request) throws Exception {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("No existe la categoria"));
        if (!category.getUser().getUserId().equals(userId)) {
            throw new Exception("No existe la categoria");
        }
        categoryMapper.updateEntity(category, request);
        CategoryEntity updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    /**
     * Eliminar categoría
     */
    public void deleteCategory(Long userId, Long categoryId) throws Exception {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("No existe la categoria"));
        if (!category.getUser().getUserId().equals(userId)) {
            throw new Exception("No se pudo eliminar la catergia");
        }
        categoryRepository.delete(category);
    }

    /**
     * Buscar entidad de categoría (para uso interno)
     */
    public CategoryEntity findCategoryEntityById(Long categoryId) throws Exception {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("No existe la categoria"));
    }
}
