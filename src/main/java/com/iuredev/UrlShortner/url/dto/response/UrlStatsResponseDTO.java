package com.iuredev.UrlShortner.url.dto.response;

import java.time.LocalDateTime;

public record UrlStatsResponseDTO(
        Long id,
        String originalUrl,
        String shortCode,
        LocalDateTime createdAt,
        Long accessCount
) {
}
