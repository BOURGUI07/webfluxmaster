package com.example.playground.sec04.dto;

import lombok.Builder;

@Builder
public record CustomerResponse(
        Integer id,
        String name,
        String email
) {
}
