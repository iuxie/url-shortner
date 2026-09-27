package com.iuredev.UrlShortner.url.controller;

import com.iuredev.UrlShortner.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "Redirecionamento", description = "Resolução de um link curto para a URL original")
@RestController
public class UrlRedirectController {

    private final UrlService service;

    public UrlRedirectController(UrlService service) {
        this.service = service;
    }

    @Operation(summary = "Redireciona para a URL original e incrementa o contador de acessos")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirecionamento realizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum link encontrado para o código informado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @Parameter(description = "Código curto do link", example = "aZ3kT9x")
            @PathVariable String shortCode
    ) {
        String originalUrl = service.resolveAndRegisterAccess(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

}