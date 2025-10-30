package com.jsca.gestor_gastos_personales_api.persistence.repository;

import com.jsca.gestor_gastos_personales_api.persistence.entities.SavingsPlanEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.PlanStatus;
import com.jsca.gestor_gastos_personales_api.util.emun.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsPlanRepository extends JpaRepository<SavingsPlanEntity, Long> {

    /**
     * Buscar todos los planes de un usuario
     */
    List<SavingsPlanEntity> findByUser_UserId(Long userId);

    /**
     * Buscar planes activos de un usuario
     */
    List<SavingsPlanEntity> findByUser_UserIdAndStatus(Long userId, PlanStatus status);

    /**
     * Buscar planes por tipo
     */
    List<SavingsPlanEntity> findByUser_UserIdAndPlanType(Long userId, PlanType planType);

    /**
     * Buscar planes cuya fecha objetivo está próxima
     */
    List<SavingsPlanEntity> findByUser_UserIdAndTargetDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Verificar si el usuario tiene un plan activo de cierto tipo
     */
    boolean existsByUser_UserIdAndPlanTypeAndStatus(
            Long userId,
            PlanType planType,
            PlanStatus status
    );

    /**
     * Buscar planes próximos a vencer (en los próximos N días)
     */
    @Query("""
        SELECT s FROM SavingsPlanEntity s 
        WHERE s.user.userId = :userId 
        AND s.status = 'ACTIVE' 
        AND s.targetDate BETWEEN CURRENT_DATE AND :endDate
        ORDER BY s.targetDate ASC
        """)
    List<SavingsPlanEntity> findUpcomingDeadlines(
            @Param("userId") Long userId,
            @Param("endDate") LocalDate endDate
    );

    /**
     *  Calcular total ahorrado por el usuario
     */
    @Query("""
        SELECT COALESCE(SUM(s.currentAmount), 0) 
        FROM SavingsPlanEntity s 
        WHERE s.user.userId = :userId 
        AND s.status = 'ACTIVE'
        """)
    BigDecimal calculateTotalSaved(@Param("userId") Long userId);

    /**
     *  Planes ordenados por progreso (los más cercanos a completarse)
     */
    @Query("""
        SELECT s FROM SavingsPlanEntity s 
        WHERE s.user.userId = :userId 
        AND s.status = 'ACTIVE' 
        ORDER BY (s.currentAmount / s.goalAmount) DESC
        """)
    List<SavingsPlanEntity> findOrderedByProgress(@Param("userId") Long userId);

    /**
     * Contar planes por estado
     */
    @Query("SELECT COUNT(s) FROM SavingsPlanEntity s WHERE s.user.userId = :userId AND s.status = :status")
    Long countByUserAndStatus(@Param("userId") Long userId, @Param("status") PlanStatus status);

    /**
     * Buscar plan específico del usuario
     */
    Optional<SavingsPlanEntity> findByPlanIdAndUser_UserId(Long planId, Long userId);
}
