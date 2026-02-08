package com.kanga.kickrush.domain.releaseSize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = {
        "com.kanga.kickrush.domain.release",
        "com.kanga.kickrush.domain.releaseSize"
})
@EnableJpaRepositories(basePackages = {
        "com.kanga.kickrush.domain.release",
        "com.kanga.kickrush.domain.releaseSize"
})
class ReleaseSizeRepositoryTest {

    @Autowired
    private ReleaseSizeRepository releaseSizeRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Test
    @DisplayName("사이즈별 재고를 저장할 수 있다")
    void shouldSaveReleaseSize() {
        // given
        Release release = createRelease();
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(release.getId())
                .size(270)
                .stock(20)
                .price(250000)
                .build();

        // when
        ReleaseSize saved = releaseSizeRepository.save(releaseSize);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getReleaseId()).isEqualTo(release.getId());
        assertThat(saved.getSize()).isEqualTo(270);
    }

    @Test
    @DisplayName("ID로 사이즈별 재고를 조회할 수 있다")
    void shouldFindReleaseSizeById() {
        // given
        Release release = createRelease();
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(release.getId())
                .size(275)
                .stock(15)
                .price(240000)
                .build();
        ReleaseSize saved = releaseSizeRepository.save(releaseSize);

        // when
        Optional<ReleaseSize> found = releaseSizeRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getReleaseId()).isEqualTo(release.getId());
        assertThat(found.get().getSize()).isEqualTo(275);
    }

    @Test
    @DisplayName("사이즈별 재고를 삭제할 수 있다")
    void shouldDeleteReleaseSize() {
        // given
        Release release = createRelease();
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(release.getId())
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

    private Release createRelease() {
        LocalDateTime now = LocalDateTime.now();
        return releaseRepository.save(Release.builder()
                .shoeId(1L)
                .releaseDateTime(now.minusMinutes(1))
                .endDateTime(now.plusMinutes(10))
                .totalStock(100)
                .build());
    }
}
