package com.iuredev.UrlShortner.url.controller;

import com.iuredev.UrlShortner.url.dto.request.UrlCreateRequestDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlResponseDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlStatsResponseDTO;
import com.iuredev.UrlShortner.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "URLs", description = "Criação de links curtos e consulta de estatísticas")
@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os links já encurtados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UrlResponseDTO>> findAllUrls() {
        return ResponseEntity.ok(service.findAllUrls());
    }

    @Operation(summary = "Cria um novo link curto a partir de uma URL original")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Link criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UrlResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "URL inválida ou ausente",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<UrlResponseDTO> createUrl(
            @Valid @RequestBody UrlCreateRequestDTO requestDTO,
            UriComponentsBuilder uriBuilder
    ) {
        UrlResponseDTO response = service.createUrl(requestDTO);

        URI location = uriBuilder
                .path("/api/urls/{shortCode}/stats")
                .buildAndExpand(response.shortCode())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Consulta estatísticas de acesso de um link pelo código curto")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estatísticas encontradas",
                    content = @Content(schema = @Schema(implementation = UrlStatsResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum link encontrado para o código informado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponseDTO> findUrlStats(
            @Parameter(description = "Código curto do link", example = "aZ3kT9x")
            @PathVariable String shortCode
    ) {
        return ResponseEntity.ok(service.findUrlStats(shortCode));
    }

}