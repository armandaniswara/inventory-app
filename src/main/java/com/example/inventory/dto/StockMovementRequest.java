package com.example.inventory.dto;

import com.example.inventory.entity.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockMovementRequest {

    @NotNull(message = "productId wajib diisi")
    private Long productId;

    @NotNull(message = "Tipe pergerakan (IN/OUT) wajib diisi")
    private MovementType type;

    @NotNull(message = "Jumlah wajib diisi")
    @Positive(message = "Jumlah harus lebih dari 0")
    private Integer quantity;

    private String note;
}
