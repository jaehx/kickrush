package com.kanga.kickrush.domain.release;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReleaseRepositoryTest {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Test
    @DisplayName("발매를 저장할 수 있다")
    void shouldSaveRelease() {
        // given
        Release release = Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.of(2026, 2, 10, 10, 0))
                .endDateTime(LocalDateTime.of(2026, 2, 10, 10, 30))
                .totalStock(100)
                .build();

        // when
        Release savedRelease = releaseRepository.save(release);

        // then
        assertThat(savedRelease.getId()).isNotNull();
        assertThat(savedRelease.getShoeId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("ID로 발매를 조회할 수 있다")
    void shouldFindReleaseById() {
        // given
        Release release = Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.of(2026, 2, 10, 10, 0))
                .endDateTime(LocalDateTime.of(2026, 2, 10, 10, 30))
                .totalStock(100)
                .build();
        Release savedRelease = releaseRepository.save(release);

        // when
        Optional<Release> foundRelease = releaseRepository.findById(savedRelease.getId());

        // then
        assertThat(foundRelease).isPresent();
        assertThat(foundRelease.get().getShoeId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("발매를 삭제할 수 있다")
    void shouldDeleteRelease() {
        // given
        Release release = Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.of(2026, 2, 10, 10, 0))
                .endDateTime(LocalDateTime.of(2026, 2, 10, 10, 30))
                .totalStock(100)
                .build();
        Release savedRelease = releaseRepository.save(release);

        // when
        releaseRepository.deleteById(savedRelease.getId());

        // then
        Optional<Release> deletedRelease = releaseRepository.findById(savedRelease.getId());
        assertThat(deletedRelease).isEmpty();
    }
}
