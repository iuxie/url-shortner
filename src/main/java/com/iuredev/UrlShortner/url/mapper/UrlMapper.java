package com.iuredev.UrlShortner.url.mapper;

import com.iuredev.UrlShortner.url.dto.request.UrlCreateRequestDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlResponseDTO;
import com.iuredev.UrlShortner.url.dto.response.UrlStatsResponseDTO;
import com.iuredev.UrlShortner.url.model.UrlModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UrlMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shortCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accessCount", ignore = true)
    UrlModel toEntity(UrlCreateRequestDTO dto);

    UrlResponseDTO toResponseDTO(UrlModel model);

    UrlStatsResponseDTO toStatsResponseDTO(UrlModel model);
}