package com.example.inventory.service;

import com.example.inventory.dto.ProductRequest;
import com.example.inventory.dto.ProductResponse;
import com.example.inventory.entity.Category;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk dengan id " + id + " tidak ditemukan"));
    }

    public ProductResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    public List<ProductResponse> search(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> findLowStock() {
        return productRepository.findLowStockProducts().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findById(id);
        applyRequest(product, request);
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }

    /**
     * Menyimpan entity Product secara langsung. Dipakai oleh StockService
     * saat mengubah jumlah stok, tanpa perlu bolak-balik lewat DTO.
     */
    public Product saveDirect(Product product) {
        return productRepository.save(product);
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock() != null ? request.getStock() : 0);
        product.setMinStock(request.getMinStock() != null ? request.getMinStock() : 5);

        if (request.getCategoryId() != null) {
            Category category = categoryService.findById(request.getCategoryId());
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getSku(),
                p.getPrice(),
                p.getStock(),
                p.getMinStock(),
                p.getStock() <= p.getMinStock(),
                p.getCategory() != null ? p.getCategory().getName() : null
        );
    }
}
