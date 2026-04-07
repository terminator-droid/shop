package com.dudev.inventoryservice.mapper;

import com.dudev.inventoryservice.api.dto.CreateProductDto;
import com.dudev.inventoryservice.api.dto.ProductDto;
import com.dudev.inventoryservice.model.Product;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductWriteOffResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    List<ProductResponse> toGrpcDto(List<Product> products);

    Product toEntity(CreateProductDto createProductDto);

    ProductDto toDto(Product product);

    List<ProductWriteOffResponse> toGrpcWriteOffDto(List<Product> products);
}