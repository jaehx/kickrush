package com.kanga.kickrushapi.order.dto;

public record CancelOrderResponse(Long id, String status, String cancelledAt) {
}
