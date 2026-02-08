package com.kanga.kickrush.domain.release;

import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
class ReleaseAssociationTest {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseSizeRepository releaseSizeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Release에서 ReleaseSize 목록을 조회할 수 있다")
    void shouldLoadReleaseSizesFromRelease() {
        LocalDateTime now = LocalDateTime.now();
        Release release = releaseRepository.save(Release.builder()
                .shoeId(1L)
                .releaseDateTime(now.minusMinutes(1))
                .endDateTime(now.plusMinutes(10))
                .totalStock(10)
                .build());

        releaseSizeRepository.save(ReleaseSize.builder()
                .releaseId(release.getId())
                .size(270)
                .stock(5)
                .price(300000)
                .build());

        entityManager.flush();
        entityManager.clear();

        Release found = releaseRepository.findById(release.getId()).orElseThrow();

        assertThat(found.getSizes()).hasSize(1);
        assertThat(found.getSizes().get(0).getReleaseId()).isEqualTo(release.getId());
    }

    @Test
    @DisplayName("ReleaseSize에서 Release를 조회할 수 있다")
    void shouldLoadReleaseFromReleaseSize() {
        LocalDateTime now = LocalDateTime.now();
        Release release = releaseRepository.save(Release.builder()
                .shoeId(2L)
                .releaseDateTime(now.minusMinutes(2))
                .endDateTime(now.plusMinutes(5))
                .totalStock(20)
                .build());

        ReleaseSize saved = releaseSizeRepository.save(ReleaseSize.builder()
                .releaseId(release.getId())
                .size(275)
                .stock(7)
                .price(280000)
                .build());

        entityManager.flush();
        entityManager.clear();

        ReleaseSize found = releaseSizeRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getRelease()).isNotNull();
        assertThat(found.getRelease().getId()).isEqualTo(release.getId());
    }
}
