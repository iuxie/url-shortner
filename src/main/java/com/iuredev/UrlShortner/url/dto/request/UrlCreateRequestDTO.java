package com.iuredev.UrlShortner.url.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UrlCreateRequestDTO(
        @NotBlank @Size(max = 2048) String originalUrl
) {
}
