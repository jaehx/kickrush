package com.kanga.kickrush.domain.releaseSize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReleaseSizeServiceTest {

    @Mock
    private ReleaseSizeRepository releaseSizeRepository;

    @InjectMocks
    private ReleaseSizeService releaseSizeService;

    @Test
    @DisplayName("ID로 사이즈별 재고를 조회할 수 있다")
    void shouldFindReleaseSizeById() {
        // given
        Long releaseSizeId = 1L;
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(270)
                .stock(30)
                .price(280000)
                .build();
        given(releaseSizeRepository.findById(releaseSizeId)).willReturn(Optional.of(releaseSize));

        // when
        ReleaseSize found = releaseSizeService.findById(releaseSizeId);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getReleaseId()).isEqualTo(1L);
        assertThat(found.getSize()).isEqualTo(270);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
    void shouldThrowExceptionWhenReleaseSizeNotFound() {
        // given
        Long missingId = 999L;
        given(releaseSizeRepository.findById(missingId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> releaseSizeService.findById(missingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사이즈별 재고를 찾을 수 없습니다");
    }
}
