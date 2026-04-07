package com.dudev.inventoryservice.service.grpc;

import com.dudev.inventoryservice.mapper.ProductMapper;
import com.dudev.inventoryservice.service.ProductService;
import com.example.grpc.InventoryServiceGrpc;
import com.example.grpc.ProductsRequest;
import com.example.grpc.ProductsResponse;
import com.example.grpc.ProductsWriteOffRequest;
import com.example.grpc.ProductsWriteOffResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Component;

/**
 * gRPC‑служба, реализующая протокол {@code InventoryService}.
 *
 * <p>Метод {@code checkAvailability} возвращает список доступных товаров,
 * а {@code writeOff} списывает указанные количества и возвращает результат.
 *
 * @see com.dudev.inventoryservice.service.ProductService
 * @see com.dudev.inventoryservice.mapper.ProductMapper
 * @since 1.0.0
 */

@Component
@RequiredArgsConstructor
@GrpcService
public class InventoryGrpc extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Override
    public void checkAvailability(ProductsRequest request, StreamObserver<ProductsResponse> responseObserver) {
        responseObserver.onNext(ProductsResponse.newBuilder()
                .addAllProducts(productMapper.toGrpcDto(
                        productService.getProducts(request.getProductsList())))
                .build());
        responseObserver.onCompleted();
    }


    @Override
    public void writeOff(ProductsWriteOffRequest productsWriteOffRequest, StreamObserver<ProductsWriteOffResponse> responseStreamObserver) {
        responseStreamObserver.onNext(ProductsWriteOffResponse.newBuilder()
                .addAllProducts(productMapper.toGrpcWriteOffDto(
                        productService.writeOffProducts(productsWriteOffRequest.getProductsList())))
                .build());
//        responseStreamObserver.onError(new NotEnoughProductsException());
        responseStreamObserver.onCompleted();
    }
}