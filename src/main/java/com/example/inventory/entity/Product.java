package com.example.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Column(nullable = false, length = 150)
    private String name;

    @Column(unique = true, length = 50)
    private String sku;

    @PositiveOrZero(message = "Harga tidak boleh negatif")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @PositiveOrZero(message = "Stok tidak boleh negatif")
    @Column(nullable = false)
    private Integer stock = 0;

    // Ambang batas minimum stok untuk peringatan "stok menipis"
    @Column(name = "min_stock", nullable = false)
    private Integer minStock = 5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
