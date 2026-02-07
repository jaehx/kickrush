package com.kanga.kickrushapi.mock;

import java.time.LocalDateTime;

public record Order(Long id, Long memberId, Long releaseSizeId, Long shoeId, int size, int price,
                    OrderStatus status, LocalDateTime orderedAt, LocalDateTime cancelledAt) {
}
