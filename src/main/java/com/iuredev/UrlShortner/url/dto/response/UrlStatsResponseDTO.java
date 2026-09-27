package com.iuredev.UrlShortner.url.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Estatísticas de acesso de um link curto")
public record UrlStatsResponseDTO(

        @Schema(description = "Identificador interno do link", example = "1")
        Long id,

        @Schema(description = "URL original associada ao link", example = "https://www.exemplo.com/pagina-com-um-caminho-bem-longo")
        String originalUrl,

        @Schema(description = "Código curto do link", example = "aZ3kT9x")
        String shortCode,

        @Schema(description = "Data e hora em que o link foi criado", example = "2026-09-27T14:32:00")
        LocalDateTime createdAt,

        @Schema(description = "Quantidade de vezes que o link foi acessado", example = "42")
        Long accessCount
) {
}