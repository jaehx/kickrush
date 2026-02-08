package com.kanga.kickrushapi.release.dto;

import java.util.List;
import com.kanga.kickrushapi.shoe.dto.ShoeDetailDto;

public record ReleaseDetailDto(Long id, ShoeDetailDto shoe, String releaseDateTime, String endDateTime,
                               String status, int totalStock, List<ReleaseSizeDto> sizes) {
}
