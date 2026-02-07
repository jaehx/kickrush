package com.kanga.kickrush.domain.release;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReleaseServiceTest {

    @Mock
    private ReleaseRepository releaseRepository;

    @InjectMocks
    private ReleaseService releaseService;

    @Test
    @DisplayName("ID로 발매를 조회할 수 있다")
    void shouldFindReleaseById() {
        // given
        Long releaseId = 1L;
        Release release = Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.of(2026, 2, 10, 10, 0))
                .endDateTime(LocalDateTime.of(2026, 2, 10, 10, 30))
                .totalStock(100)
                .build();
        given(releaseRepository.findById(releaseId)).willReturn(Optional.of(release));

        // when
        Release foundRelease = releaseService.findById(releaseId);

        // then
        assertThat(foundRelease).isNotNull();
        assertThat(foundRelease.getShoeId()).isEqualTo(1L);
        assertThat(foundRelease.getTotalStock()).isEqualTo(100);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
    void shouldThrowExceptionWhenReleaseNotFound() {
        // given
        Long nonExistentId = 999L;
        given(releaseRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> releaseService.findById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("발매를 찾을 수 없습니다");
    }
}
