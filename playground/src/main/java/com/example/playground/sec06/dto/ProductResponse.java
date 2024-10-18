package com.example.playground.sec06.dto;

import lombok.Builder;

@Builder
public record ProductResponse(
        Integer id,
        String description,
        Integer price
) {
}
