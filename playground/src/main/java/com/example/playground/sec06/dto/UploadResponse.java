package com.example.playground.sec06.dto;

import java.util.UUID;

public record UploadResponse(
        UUID confirmationId,
        Long productCount
) {
}
