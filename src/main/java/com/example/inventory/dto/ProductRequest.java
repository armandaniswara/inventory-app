package com.example.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Nama produk tidak boleh kosong")
    private String name;

    private String sku;

    @PositiveOrZero(message = "Harga tidak boleh negatif")
    private BigDecimal price;

    @PositiveOrZero(message = "Stok awal tidak boleh negatif")
    private Integer stock = 0;

    private Integer minStock = 5;

    private Long categoryId;
}
