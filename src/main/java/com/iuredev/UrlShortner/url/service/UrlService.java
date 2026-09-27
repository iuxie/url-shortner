package com.iuredev.UrlShortner.url.service;

import com.iuredev.UrlShortner.url.dto.request.UrlCreateRequestDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlResponseDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlStatsResponseDTO;
import com.iuredev.UrlShortner.url.exception.UrlNotFoundException;
import com.iuredev.UrlShortner.url.mapper.UrlMapper;
import com.iuredev.UrlShortner.url.model.UrlModel;
import com.iuredev.UrlShortner.url.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UrlService {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 7;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UrlMapper mapper;
    private final UrlRepository repository;

    public UrlService(UrlMapper mapper, UrlRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<UrlResponseDTO> findAllUrls() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public UrlResponseDTO createUrl(UrlCreateRequestDTO requestDTO) {
        UrlModel urlModel = mapper.toEntity(requestDTO);
        urlModel.setShortCode(generateUniqueShortCode());

        UrlModel savedModel = repository.save(urlModel);
        return mapper.toResponseDTO(savedModel);
    }

    public UrlStatsResponseDTO findUrlStats(String shortCode) {
        UrlModel urlModel = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        return mapper.toStatsResponseDTO(urlModel);
    }

    @Transactional
    public String resolveAndRegisterAccess(String shortCode) {
        UrlModel urlModel = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        repository.incrementAccessCount(shortCode);
        return urlModel.getOriginalUrl();
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (repository.existsByShortCode(shortCode));

        return shortCode;
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

}