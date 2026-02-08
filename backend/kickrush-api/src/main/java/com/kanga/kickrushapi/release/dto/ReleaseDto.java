package com.kanga.kickrushapi.release.dto;

import com.kanga.kickrushapi.shoe.dto.ShoeSummaryDto;
public record ReleaseDto(Long id, ShoeSummaryDto shoe, String releaseDateTime, String endDateTime,
                         String status, int totalStock) {
}
