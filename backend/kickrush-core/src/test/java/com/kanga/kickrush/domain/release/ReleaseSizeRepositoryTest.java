package com.kanga.kickrush.domain.release;

import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseSizeRepository releaseSizeRepository;

    @Test
    @DisplayName("사이즈별 재고를 저장하고 조회할 수 있다")
    void shouldSaveAndFindReleaseSize() {
        Release release = createRelease();
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(release.getId())
                .size(270)
                .stock(20)
                .price(250000)
                .build();

        ReleaseSize saved = releaseSizeRepository.save(releaseSize);
        Optional<ReleaseSize> found = releaseSizeRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getReleaseId()).isEqualTo(release.getId());
        assertThat(found.get().getSize()).isEqualTo(270);
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
