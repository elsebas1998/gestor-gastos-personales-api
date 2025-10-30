package com.jsca.gestor_gastos_personales_api.persistence.entities;

import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions",
        indexes = {
                @Index(name = "idx_transactions_user", columnList = "user_id"),
                @Index(name = "idx_transactions_date", columnList = "transaction_date"),
                @Index(name = "idx_transactions_user_date", columnList = "user_id, transaction_date"),
                @Index(name = "idx_transactions_category", columnList = "category_id"),
                @Index(name = "idx_transactions_type", columnList = "type")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @PastOrPresent
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_transaction_user"))
    @NotNull
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_transaction_category"))
    private CategoryEntity category;

    @PrePersist
    @PreUpdate
    private void validateTypeConsistency() {
        if (category != null && !type.equals(category.getType())) {
            throw new IllegalStateException(
                    String.format("Transaction type %s does not match category type %s",
                            type, category.getType())
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionEntity)) return false;
        TransactionEntity that = (TransactionEntity) o;
        return transactionId != null && transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}