package com.example.playground.sec04.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedCustomerResponse(
        List<CustomerResponse> response,
        Long count
) {
}
