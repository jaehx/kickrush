package com.kanga.kickrushapi.order.dto;

public record OrderDto(Long id, ShoeSummaryWithImageDto shoe, int size, int price,
                       String status, String orderedAt) {
}
