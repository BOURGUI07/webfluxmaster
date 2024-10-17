package com.example.playground.sec03.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedCustomerResponse(
        List<CustomerResponse> response,
        Long count
) {
}
