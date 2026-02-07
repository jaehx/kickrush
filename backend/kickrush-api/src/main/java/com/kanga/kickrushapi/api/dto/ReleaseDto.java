package com.kanga.kickrushapi.api.dto;

public record ReleaseDto(Long id, ShoeSummaryDto shoe, String releaseDateTime, String endDateTime,
                         String status, int totalStock) {
}
