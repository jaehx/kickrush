package com.kanga.kickrush.domain.releaseSize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReleaseSizeTest {

    @Test
    @DisplayName("사이즈별 재고를 생성할 수 있다")
    void shouldCreateReleaseSize() {
        // given
        Long releaseId = 1L;
        int size = 270;
        int stock = 50;
        int price = 300000;

        // when
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(releaseId)
                .size(size)
                .stock(stock)
                .price(price)
                .build();

        // then
        assertThat(releaseSize).isNotNull();
        assertThat(releaseSize.getReleaseId()).isEqualTo(releaseId);
        assertThat(releaseSize.getSize()).isEqualTo(size);
        assertThat(releaseSize.getStock()).isEqualTo(stock);
        assertThat(releaseSize.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("허용되지 않는 사이즈는 생성할 수 없다")
    void shouldRejectInvalidSize() {
        // given
        Long releaseId = 1L;
        int invalidSize = 233;

        // when & then
        assertThatThrownBy(() -> ReleaseSize.builder()
                .releaseId(releaseId)
                .size(invalidSize)
                .stock(10)
                .price(200000)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 사이즈");
    }
}
