package com.example.inventory.controller;

import com.example.inventory.dto.StockMovementRequest;
import com.example.inventory.dto.StockMovementResponse;
import com.example.inventory.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockMovementResponse record(@Valid @RequestBody StockMovementRequest request) {
        return stockService.recordMovement(request);
    }

    @GetMapping
    public List<StockMovementResponse> getAll() {
        return stockService.findAll();
    }

    @GetMapping("/product/{productId}")
    public List<StockMovementResponse> getByProduct(@PathVariable Long productId) {
        return stockService.findByProduct(productId);
    }
}
