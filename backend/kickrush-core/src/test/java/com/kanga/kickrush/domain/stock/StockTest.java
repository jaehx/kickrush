package com.kanga.kickrush.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockTest {

    @Test
    @DisplayName("재고를 생성할 수 있다")
    void shouldCreateStock() {
        // given
        Long releaseId = 1L;
        String size = "270";
        int quantity = 50;

        // when
        Stock stock = Stock.builder()
                .releaseId(releaseId)
                .size(size)
                .quantity(quantity)
                .build();

        // then
        assertThat(stock).isNotNull();
        assertThat(stock.getReleaseId()).isEqualTo(releaseId);
        assertThat(stock.getSize()).isEqualTo(size);
        assertThat(stock.getQuantity()).isEqualTo(quantity);
    }
}
