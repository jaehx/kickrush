package com.kanga.kickrush.domain.release;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReleaseTest {

    @Test
    @DisplayName("발매를 생성할 수 있다")
    void shouldCreateRelease() {
        // given
        Long shoeId = 1L;
        LocalDateTime releaseDateTime = LocalDateTime.of(2026, 2, 10, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 2, 10, 10, 30);
        int totalStock = 100;

        // when
        Release release = Release.builder()
                .shoeId(shoeId)
                .releaseDateTime(releaseDateTime)
                .endDateTime(endDateTime)
                .totalStock(totalStock)
                .build();

        // then
        assertThat(release).isNotNull();
        assertThat(release.getShoeId()).isEqualTo(shoeId);
        assertThat(release.getReleaseDateTime()).isEqualTo(releaseDateTime);
        assertThat(release.getEndDateTime()).isEqualTo(endDateTime);
        assertThat(release.getTotalStock()).isEqualTo(totalStock);
    }
}
