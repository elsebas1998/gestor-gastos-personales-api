package com.jsca.gestor_gastos_personales_api.persistence.entities;


import com.jsca.gestor_gastos_personales_api.util.emun.PlanStatus;
import com.jsca.gestor_gastos_personales_api.util.emun.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "savings_plans",
        indexes = {
                @Index(name = "idx_savings_user", columnList = "user_id"),
                @Index(name = "idx_savings_status", columnList = "status")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsPlanEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "goal_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal goalAmount;

    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "current_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Future
    @Column(name = "target_date")
    private LocalDate targetDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 50)
    private PlanType planType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PlanStatus status = PlanStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_savings_plan_user"))
    @NotNull(message = "User is required")
    private UserEntity user;

    @Transient
    public Integer calculateProgress() {
        if (goalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        BigDecimal progress = currentAmount
                .divide(goalAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return progress.intValue();
    }

    public void addAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.currentAmount = this.currentAmount.add(amount);

        if (this.currentAmount.compareTo(this.goalAmount) >= 0) {
            this.status = PlanStatus.COMPLETED;
        }
    }

    public void subtractAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.currentAmount.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds in savings plan");
        }
        this.currentAmount = this.currentAmount.subtract(amount);

        // Reactivar si estaba completado
        if (this.status == PlanStatus.COMPLETED &&
                this.currentAmount.compareTo(this.goalAmount) < 0) {
            this.status = PlanStatus.ACTIVE;
        }
    }

    @PrePersist
    @PreUpdate
    private void validateAmounts() {
        if (currentAmount.compareTo(goalAmount) > 0) {
            throw new IllegalStateException("Current amount cannot exceed goal amount");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavingsPlanEntity)) return false;
        SavingsPlanEntity that = (SavingsPlanEntity) o;
        return planId != null && planId.equals(that.planId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}