package com.jsca.gestor_gastos_personales_api.persistence.repository;

import com.jsca.gestor_gastos_personales_api.persistence.entities.TransactionEntity;
import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * Buscar todas las transacciones de un usuario
     * Con paginación
     */
    Page<TransactionEntity> findByUser_UserId(Long userId, Pageable pageable);

    /**
     * Buscar transacciones por usuario y tipo
     */
    List<TransactionEntity> findByUser_UserIdAndType(Long userId, TransactionType type);

    /**
     * Buscar transacciones por usuario en rango de fechas
     */
    List<TransactionEntity> findByUser_UserIdAndTransactionDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Buscar transacciones por usuario, tipo y rango de fechas
     */
    List<TransactionEntity> findByUser_UserIdAndTypeAndTransactionDateBetween(
            Long userId,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Buscar transacciones por categoría
     */
    List<TransactionEntity> findByCategory_CategoryId(Long categoryId);

    /**
     * Buscar transacciones por usuario y categoría
     */
    List<TransactionEntity> findByUser_UserIdAndCategory_CategoryId(Long userId, Long categoryId);

    // ==================== QUERIES JPQL PERSONALIZADAS ====================


    /**
     * Transacciones del mes actual
     */
    @Query("""
        SELECT t FROM TransactionEntity t 
        WHERE t.user.userId = :userId 
        AND YEAR(t.transactionDate) = :year 
        AND MONTH(t.transactionDate) = :month
        """)
    List<TransactionEntity> findByUserAndMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month
    );

    /**
     * Buscar transacciones con monto mayor a un valor
     */
    @Query("SELECT t FROM TransactionEntity t WHERE t.user.userId = :userId AND t.amount >= :minAmount")
    List<TransactionEntity> findLargeTransactions(
            @Param("userId") Long userId,
            @Param("minAmount") BigDecimal minAmount
    );

    /**
     * Buscar transacciones que contengan texto en descripción
     */
    @Query("""
        SELECT t FROM TransactionEntity t 
        WHERE t.user.userId = :userId 
        AND LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<TransactionEntity> searchByDescription(
            @Param("userId") Long userId,
            @Param("keyword") String keyword
    );

    /**
     * Ultimas N transacciones de un usuario
     */
    Page<TransactionEntity> findByUser_UserIdOrderByTransactionDateDesc(Long userId, Pageable pageable);
}
