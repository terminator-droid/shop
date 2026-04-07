package com.dudev.inventoryservice.service;

import com.dudev.inventoryservice.api.dto.CreateProductDto;
import com.dudev.inventoryservice.api.dto.ProductDto;
import com.dudev.inventoryservice.model.Product;
import com.example.grpc.ProductRequest;
import com.example.grpc.ProductWriteOffRequest;

import java.util.List;

public interface ProductService {

    List<Product> getProducts(List<ProductRequest> productsList);

    List<ProductDto> getProductsDto();

    ProductDto findById(String productId);

    void createProduct(CreateProductDto productDto);

    void deleteProduct(String productId);

    List<Product> writeOffProducts(List<ProductWriteOffRequest> productsList);
}
