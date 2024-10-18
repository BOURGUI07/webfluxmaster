package com.example.playground.sec06.dto;

import lombok.Builder;

@Builder
public record ProductRequest(
        String description,
        Integer price
) {
}
