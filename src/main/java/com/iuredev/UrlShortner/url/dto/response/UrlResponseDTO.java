package com.iuredev.UrlShortner.url.dto.response;

public record UrlResponseDTO(
        Long id,
        String originalUrl,
        String shortCode
) {
}
