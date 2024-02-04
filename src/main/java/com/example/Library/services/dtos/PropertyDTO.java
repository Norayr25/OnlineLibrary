package com.example.Library.services.dtos;

import lombok.Builder;

@Builder
public record PropertyDTO(String key, Object value) {
}