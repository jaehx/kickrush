package com.kanga.kickrushapi.api.dto;

public record AdminReleaseCreateResponse(Long id, Long shoeId, String releaseDateTime, int totalStock) {
}
