package com.kanga.kickrush.domain.order;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void shouldCreateOrder() {
        LocalDateTime orderedAt = LocalDateTime.of(2026, 2, 10, 11, 0);

        Order order = Order.builder()
                .memberId(1L)
                .releaseSizeId(2L)
                .status(OrderStatus.COMPLETED)
                .orderedAt(orderedAt)
                .build();

        assertThat(order.getMemberId()).isEqualTo(1L);
        assertThat(order.getReleaseSizeId()).isEqualTo(2L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(order.getOrderedAt()).isEqualTo(orderedAt);
    }
}
