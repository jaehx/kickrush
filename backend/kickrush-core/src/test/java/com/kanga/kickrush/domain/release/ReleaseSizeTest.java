package com.kanga.kickrush.domain.release;

import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReleaseSizeTest {

    @Test
    @DisplayName("사이즈별 재고를 생성할 수 있다")
    void shouldCreateReleaseSize() {
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(270)
                .stock(50)
                .price(300000)
                .build();

        assertThat(releaseSize).isNotNull();
        assertThat(releaseSize.getReleaseId()).isEqualTo(1L);
        assertThat(releaseSize.getSize()).isEqualTo(270);
        assertThat(releaseSize.getStock()).isEqualTo(50);
        assertThat(releaseSize.getPrice()).isEqualTo(300000);
    }

    @Test
    @DisplayName("허용되지 않는 사이즈는 생성할 수 없다")
    void shouldRejectInvalidSize() {
        assertThatThrownBy(() -> ReleaseSize.builder()
                .releaseId(1L)
                .size(233)
                .stock(10)
                .price(200000)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 사이즈");
    }
}
