package com.example.inventory.repository;

import com.example.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuIgnoreCase(String sku);

    List<Product> findByCategoryId(Long categoryId);

    // Produk yang stoknya sudah di bawah atau sama dengan ambang batas minimum
    @Query("SELECT p FROM Product p WHERE p.stock <= p.minStock")
    List<Product> findLowStockProducts();

    List<Product> findByNameContainingIgnoreCase(String name);
}
