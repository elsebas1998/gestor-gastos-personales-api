package com.jsca.gestor_gastos_personales_api.persistence.entities;

import com.jsca.gestor_gastos_personales_api.util.emun.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories",
        indexes = {
                @Index(name = "idx_categories_user", columnList = "user_id"),
                @Index(name = "idx_categories_type", columnList = "type"),
                @Index(name = "idx_categories_user_type", columnList = "user_id, type")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_category",
                        columnNames = {"user_id", "name", "type"})
        }
)
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type;

    @Column(name = "is_fixed", nullable = false)
    @Builder.Default
    private Boolean isFixed = false;

    @Size(max = 50)
    @Column(name = "icon", length = 50)
    private String icon;

    @Size(max = 20)
    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "description")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_category_user"))
    @NotNull
    private UserEntity user;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<TransactionEntity> transactions = new HashSet<>();

    public void addTransaction(TransactionEntity transaction) {
        transactions.add(transaction);
        transaction.setCategory(this);
    }

    public void removeTransaction(TransactionEntity transaction) {
        transactions.remove(transaction);
        transaction.setCategory(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity)) return false;
        CategoryEntity that = (CategoryEntity) o;
        return categoryId != null && categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
