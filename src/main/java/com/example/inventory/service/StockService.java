package com.example.inventory.service;

import com.example.inventory.dto.StockMovementRequest;
import com.example.inventory.dto.StockMovementResponse;
import com.example.inventory.entity.MovementType;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.StockMovement;
import com.example.inventory.exception.InsufficientStockException;
import com.example.inventory.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductService productService;

    /**
     * Mencatat pergerakan stok (IN/OUT) dan meng-update stok produk secara atomik.
     * @Transactional memastikan update stok + insert riwayat terjadi bersamaan;
     * jika salah satu gagal, keduanya dibatalkan (rollback).
     */
    @Transactional
    public StockMovementResponse recordMovement(StockMovementRequest request) {
        Product product = productService.findById(request.getProductId());

        int currentStock = product.getStock();
        int newStock;

        if (request.getType() == MovementType.IN) {
            newStock = currentStock + request.getQuantity();
        } else { // OUT
            newStock = currentStock - request.getQuantity();
            if (newStock < 0) {
                throw new InsufficientStockException(
                        "Stok tidak cukup. Stok saat ini: " + currentStock +
                                ", jumlah yang diminta keluar: " + request.getQuantity());
            }
        }

        product.setStock(newStock);
        // ProductRepository.save dipanggil implisit karena entity managed dalam transaksi (dirty checking),
        // tapi kita save explicit lewat productService untuk kejelasan alur.
        Product savedProduct = productService.saveDirect(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(savedProduct);
        movement.setType(request.getType());
        movement.setQuantity(request.getQuantity());
        movement.setNote(request.getNote());
        movement.setStockAfter(newStock);

        StockMovement saved = stockMovementRepository.save(movement);
        return toResponse(saved);
    }

    public List<StockMovementResponse> findAll() {
        return stockMovementRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StockMovementResponse> findByProduct(Long productId) {
        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(this::toResponse)
                .toList();
    }

    private StockMovementResponse toResponse(StockMovement m) {
        return new StockMovementResponse(
                m.getId(),
                m.getProduct().getId(),
                m.getProduct().getName(),
                m.getType(),
                m.getQuantity(),
                m.getStockAfter(),
                m.getNote(),
                m.getCreatedAt()
        );
    }
}
