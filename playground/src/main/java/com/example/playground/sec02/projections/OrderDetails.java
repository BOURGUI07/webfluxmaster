package com.example.playground.sec02.projections;

import java.time.Instant;
import java.util.UUID;

public record  OrderDetails(
        UUID orderId,
        String customerName,
        String productName,
        Integer amount,
        Instant orderDate

) {
}
