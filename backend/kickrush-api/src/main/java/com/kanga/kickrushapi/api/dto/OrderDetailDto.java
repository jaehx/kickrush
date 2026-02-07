package com.kanga.kickrushapi.api.dto;

public record OrderDetailDto(Long id, ShoeSummaryWithImageAndModelDto shoe, int size, int price,
                             String status, String orderedAt, String cancelledAt) {
}
