package com.example.playground.sec05.dto;

import lombok.Builder;

@Builder
public record Product(
        Integer id,
        String description,
        Integer price
) {
}
