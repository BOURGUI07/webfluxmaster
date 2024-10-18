package com.example.playground.sec07.dto;

import lombok.Builder;

@Builder
public record ProductRequest(
        String description,
        Integer price
) {
}
