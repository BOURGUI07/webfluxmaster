package com.example.playground.sec07.dto;

import lombok.Builder;

@Builder
public record ProductResponse(
        Integer id,
        String description,
        Integer price
) {
}
