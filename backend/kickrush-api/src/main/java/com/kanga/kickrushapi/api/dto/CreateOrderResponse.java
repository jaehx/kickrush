package com.kanga.kickrushapi.api.dto;

public record CreateOrderResponse(Long id, Long releaseSizeId, int size, ShoeMiniDto shoe,
                                  int price, String status, String orderedAt) {
}
