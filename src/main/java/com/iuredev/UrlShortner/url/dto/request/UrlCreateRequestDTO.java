package com.iuredev.UrlShortner.url.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para gerar um link curto")
public record UrlCreateRequestDTO(

        @Schema(description = "URL original que será encurtada", example = "https://www.exemplo.com/pagina-com-um-caminho-bem-longo")
        @NotBlank @Size(max = 2048)
        String originalUrl
) {
}