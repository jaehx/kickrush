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
    @DisplayName("ID로 재고 수량을 조회할 수 있다")
    void shouldGetStockById() {
        // given
        Long releaseSizeId = 1L;
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(260)
                .stock(15)
                .price(250000)
                .build();
        given(releaseSizeRepository.findById(releaseSizeId)).willReturn(Optional.of(releaseSize));

        // when
        int stock = releaseSizeService.getStock(releaseSizeId);

        // then
        assertThat(stock).isEqualTo(15);
    }

    @Test
    @DisplayName("재고를 차감할 수 있다")
    void shouldDecreaseStock() {
        // given
        Long releaseSizeId = 1L;
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(255)
                .stock(10)
                .price(240000)
                .build();
        given(releaseSizeRepository.findById(releaseSizeId)).willReturn(Optional.of(releaseSize));

        // when
        ReleaseSize updated = releaseSizeService.decreaseStock(releaseSizeId, 3);

        // then
        assertThat(updated.getStock()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고가 부족하면 예외가 발생한다")
    void shouldThrowWhenStockInsufficient() {
        // given
        Long releaseSizeId = 1L;
        ReleaseSize releaseSize = ReleaseSize.builder()
                .releaseId(1L)
                .size(250)
                .stock(2)
                .price(230000)
                .build();
        given(releaseSizeRepository.findById(releaseSizeId)).willReturn(Optional.of(releaseSize));

        // when & then
        assertThatThrownBy(() -> releaseSizeService.decreaseStock(releaseSizeId, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고가 부족합니다");
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
