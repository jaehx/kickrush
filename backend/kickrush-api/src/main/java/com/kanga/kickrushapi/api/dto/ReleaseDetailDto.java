package com.kanga.kickrushapi.api.dto;

import java.util.List;

public record ReleaseDetailDto(Long id, ShoeDetailDto shoe, String releaseDateTime, String endDateTime,
                               String status, int totalStock, List<ReleaseSizeDto> sizes) {
}
