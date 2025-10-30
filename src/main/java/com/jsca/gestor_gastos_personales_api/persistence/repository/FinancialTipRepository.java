package com.jsca.gestor_gastos_personales_api.persistence.repository;


import com.jsca.gestor_gastos_personales_api.persistence.entities.FinancialTipEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.TipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTipRepository extends JpaRepository<FinancialTipEntity, Long> {

    /**
     * Buscar todos los consejos activos
     */
    List<FinancialTipEntity> findByIsActiveTrue();

    /**
     * Buscar consejos por tipo
     */
    List<FinancialTipEntity> findByTipTypeAndIsActiveTrue(TipType tipType);

    /**
     * Buscar consejos por categoría
     */
    List<FinancialTipEntity> findByCategoryAndIsActiveTrue(String category);

    /**
     *  Obtener un consejo aleatorio
     */
    @Query(value = "SELECT * FROM financial_tips WHERE is_active = true ORDER BY RANDOM() LIMIT 1",
            nativeQuery = true)
    Optional<FinancialTipEntity> findRandomTip();

    /**
     * Query JPQL: Obtener N consejos aleatorios
     */
    @Query(value = "SELECT * FROM financial_tips WHERE is_active = true ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<FinancialTipEntity> findRandomTips(@Param("limit") int limit);

    /**
     *  Obtener consejo aleatorio de un tipo específico
     */
    @Query(value = """
        SELECT * FROM financial_tips 
        WHERE is_active = true 
        AND tip_type = :tipType 
        ORDER BY RANDOM() 
        LIMIT 1
        """, nativeQuery = true)
    Optional<FinancialTipEntity> findRandomTipByType(@Param("tipType") String tipType);

    /**
     * Contar consejos activos
     */
    Long countByIsActiveTrue();
}
