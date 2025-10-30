package com.jsca.gestor_gastos_personales_api.persistence.entities;

import com.jsca.gestor_gastos_personales_api.util.emun.TipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "financial_tips",
        indexes = {
                @Index(name = "idx_tips_active", columnList = "is_active"),
                @Index(name = "idx_tips_category", columnList = "category")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTipEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tip_id")
    private Long tipId;

    @NotBlank
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_type", length = 50)
    private TipType tipType;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinancialTipEntity)) return false;
        FinancialTipEntity that = (FinancialTipEntity) o;
        return tipId != null && tipId.equals(that.tipId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}