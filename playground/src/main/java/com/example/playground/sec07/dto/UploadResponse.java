package com.example.playground.sec07.dto;

import java.util.UUID;

public record UploadResponse(
        UUID confirmationId,
        Long productCount
) {
}
