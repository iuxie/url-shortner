package com.iuredev.UrlShortner.url.controller;

import com.iuredev.UrlShortner.url.dto.request.UrlCreateRequestDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlResponseDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlStatsResponseDTO;
import com.iuredev.UrlShortner.url.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UrlResponseDTO>> findAllUrls() {
        return ResponseEntity.ok(service.findAllUrls());
    }

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

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponseDTO> findUrlStats(
            @PathVariable String shortCode
    ) {
        return ResponseEntity.ok(service.findUrlStats(shortCode));
    }

}