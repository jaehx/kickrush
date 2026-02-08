package com.kanga.kickrushapi.order.dto;

public record CreateOrderResponse(Long id, Long releaseSizeId, int size, ShoeMiniDto shoe,
                                  int price, String status, String orderedAt) {
}
