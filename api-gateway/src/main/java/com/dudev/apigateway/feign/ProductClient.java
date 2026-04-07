package com.dudev.apigateway.feign;

import com.dudev.inventoryservice.api.dto.CreateProductDto;
import com.dudev.inventoryservice.api.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign‑клиент для работы с продуктами микросервиса {@code inventory-service}.
 *
 * @since 1.0.0
 */

@FeignClient(name = "inventory-client")
public interface ProductClient {

    @GetMapping("/api/products")
    ResponseEntity<List<ProductDto>> getProducts();

    @PostMapping("/api/products")
    ResponseEntity<Void> createProduct(@RequestBody CreateProductDto productDto);

    @DeleteMapping("/api/products/{productId}")
    ResponseEntity<Void> deleteProduct(@PathVariable String productId);

    @GetMapping("/api/products/{productId}")
    ResponseEntity<ProductDto> getProduct(@PathVariable String productId);
}
