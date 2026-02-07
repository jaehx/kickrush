package com.kanga.kickrush.domain.releaseSize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReleaseSizeRepositoryTest {

    @Autowired
    private ReleaseSizeRepository releaseSizeRepository;

    @Test
    @DisplayName("사이즈별 재고를 저장할 수 있다")
    void shouldSaveReleaseSize() {
        // given
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(270)
                .stock(20)
                .price(250000)
                .build();

        // when
        ReleaseSize saved = releaseSizeRepository.save(releaseSize);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getReleaseId()).isEqualTo(1L);
        assertThat(saved.getSize()).isEqualTo(270);
    }

    @Test
    @DisplayName("ID로 사이즈별 재고를 조회할 수 있다")
    void shouldFindReleaseSizeById() {
        // given
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(275)
                .stock(15)
                .price(240000)
                .build();
        ReleaseSize saved = releaseSizeRepository.save(releaseSize);

        // when
        Optional<ReleaseSize> found = releaseSizeRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getReleaseId()).isEqualTo(1L);
        assertThat(found.get().getSize()).isEqualTo(275);
    }

    @Test
    @DisplayName("사이즈별 재고를 삭제할 수 있다")
    void shouldDeleteReleaseSize() {
        // given
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(2L)
                .size(280)
                .stock(5)
                .price(230000)
                .build();
        ReleaseSize saved = releaseSizeRepository.save(releaseSize);

        // when
        releaseSizeRepository.deleteById(saved.getId());

        // then
        Optional<ReleaseSize> deleted = releaseSizeRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }
}
