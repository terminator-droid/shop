package com.dudev.inventoryservice.service.impl;

import com.dudev.inventoryservice.api.dto.CreateProductDto;
import com.dudev.inventoryservice.api.dto.ProductDto;
import com.dudev.inventoryservice.exception.NotEnoughProductsException;
import com.dudev.inventoryservice.exception.ProductNotFoundException;
import com.dudev.inventoryservice.mapper.ProductMapper;
import com.dudev.inventoryservice.model.Product;
import com.dudev.inventoryservice.repository.ProductRepository;
import com.dudev.inventoryservice.service.ProductService;
import com.example.grpc.ProductRequest;
import com.example.grpc.ProductWriteOffRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация бизнес‑логики управления товарами.
 *
 * <p>Поддерживает:
 * <ul>
 *   <li>получение товаров по списку запросов (gRPC);</li>
 *   <li>получение всех товаров для REST‑API;</li>
 *   <li>поиск товара по ID;</li>
 *   <li>создание и удаление товара;</li>
 *   <li>списание (write‑off) товаров с пессимистической блокировкой.</li>
 * </ul>
 *
 * @see com.dudev.inventoryservice.mapper.ProductMapper
 * @see com.dudev.inventoryservice.repository.ProductRepository
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<Product> getProducts(List<ProductRequest> productsList) {
        return productRepository.findAllById(productsList.stream()
                .map(ProductRequest::getId)
                .map(UUID::fromString)
                .toList());
    }

    @Override
    public List<ProductDto> getProductsDto() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductDto findById(String productId) {
        return productRepository.findById(UUID.fromString(productId))
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    @Override
    public void createProduct(CreateProductDto productDto) {
        productRepository.save(productMapper.toEntity(productDto));
    }

    @Transactional
    @Override
    public void deleteProduct(String productId) {
        productRepository.delete(productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ProductNotFoundException(productId)));
    }

    @Transactional
    @Override
    public List<Product> writeOffProducts(List<ProductWriteOffRequest> productsList) {
        Map<String, Integer> quantities = productsList.stream()
                .collect(Collectors.toMap(ProductWriteOffRequest::getId, ProductWriteOffRequest::getQuantity));

        List<Product> products = productRepository.findAllByIdWithLock(productsList.stream()
                .map(ProductWriteOffRequest::getId)
                .map(UUID::fromString)
                .toList());

        if (products.stream()
                .anyMatch(product -> product.getQuantity() < quantities.get(product.getId().toString()))) {
            throw new NotEnoughProductsException();
        }

        products
                .forEach(product -> product.setQuantity(product.getQuantity() - quantities.get(product.getId().toString())));
        return productRepository.saveAllAndFlush(products);
    }
}
