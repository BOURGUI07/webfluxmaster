package com.example.playground.sec06.mapper;

import com.example.playground.sec06.dto.ProductRequest;
import com.example.playground.sec06.dto.ProductResponse;
import com.example.playground.sec06.entity.Product;

public class ProductMapper {
    public static Product toEntity(ProductRequest request){
        return Product
                .builder()
                .price(request.price())
                .description(request.description())
                .build();
    }

    public static ProductResponse toResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
