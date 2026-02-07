package com.kanga.kickrushapi.api.dto;

public record CancelOrderResponse(Long id, String status, String cancelledAt) {
}
