package com.iuredev.UrlShortner.url.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um link curto recém-criado")
public record UrlResponseDTO(

        @Schema(description = "Identificador interno do link", example = "1")
        Long id,

        @Schema(description = "URL original informada pelo usuário", example = "https://www.exemplo.com/pagina-com-um-caminho-bem-longo")
        String originalUrl,

        @Schema(description = "Código curto gerado, usado para redirecionamento", example = "aZ3kT9x")
        String shortCode
) {
}