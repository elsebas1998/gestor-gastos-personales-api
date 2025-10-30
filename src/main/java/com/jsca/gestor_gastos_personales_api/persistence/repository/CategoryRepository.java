package com.jsca.gestor_gastos_personales_api.persistence.repository;

import com.jsca.gestor_gastos_personales_api.persistence.entities.CategoryEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Buscar todas las categorías de un usuario
     */
    List<CategoryEntity> findByUser_UserId(Long userId);

    /**
     * Buscar categorías por usuario y tipo (INGRESO/EGRESO)
     */
    List<CategoryEntity> findByUser_UserIdAndType(Long userId, TransactionType type);

    /**
     * Buscar categorías fijas de un usuario
     */
    List<CategoryEntity> findByUser_UserIdAndIsFixedTrue(Long userId);

    /**
     * Buscar categorías variables de un usuario
     */
    List<CategoryEntity> findByUser_UserIdAndIsFixedFalse(Long userId);

    /**
     * Verificar si existe una categoría con ese nombre y tipo para el usuario
     */
    boolean existsByUser_UserIdAndNameAndType(Long userId, String name, TransactionType type);

    /**
     * Buscar una categoría específica por nombre, tipo y usuario
     */
    Optional<CategoryEntity> findByUser_UserIdAndNameAndType(
            Long userId,
            String name,
            TransactionType type
    );

    /**
     *  Categorías ordenadas por nombre
     */
    @Query("SELECT c FROM CategoryEntity c WHERE c.user.userId = :userId ORDER BY c.name ASC")
    List<CategoryEntity> findCategoriesByUserOrderedByName(@Param("userId") Long userId);

    /**
     * Contar categorías por tipo
     */
    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.user.userId = :userId AND c.type = :type")
    Long countByUserAndType(@Param("userId") Long userId, @Param("type") TransactionType type);

    /**
     * Buscar categorías más usadas
     */
    @Query("""
        SELECT c FROM CategoryEntity c 
        LEFT JOIN c.transactions t 
        WHERE c.user.userId = :userId 
        GROUP BY c.categoryId 
        ORDER BY COUNT(t) DESC
        """)
    List<CategoryEntity> findMostUsedCategories(@Param("userId") Long userId);
}
