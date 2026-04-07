package com.dudev.apigateway.controller;

import com.dudev.apigateway.feign.ProductClient;
import com.dudev.inventoryservice.api.ProductsApi;
import com.dudev.inventoryservice.api.dto.CreateProductDto;
import com.dudev.inventoryservice.api.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер, открывающий CRUD‑операции над продуктами через
 * {@link com.dudev.apigateway.feign.ProductClient}, который обращается
 * к микросервису {@code inventory-service}.
 *
 * @see com.dudev.apigateway.feign.ProductClient
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductClient productClient;

    @Override
    public ResponseEntity<Void> createProduct(CreateProductDto productDto) {
        return productClient.createProduct(productDto);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String productId) {
        return productClient.deleteProduct(productId);
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(String productId) {
        return productClient.getProduct(productId);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProducts() {
        return productClient.getProducts();
    }
}
