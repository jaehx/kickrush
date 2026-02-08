package com.kanga.kickrush.domain.order;

import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseRepository;
import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReleaseRepository releaseRepository;

    @Mock
    private ReleaseSizeRepository releaseSizeRepository;

    @Mock
    private ReleaseSizeService releaseSizeService;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void shouldCreateOrder() {
        Long memberId = 1L;
        Long releaseSizeId = 10L;
        ReleaseSize releaseSize = releaseSize(5L);
        Release release = activeRelease();

        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(false);
        given(releaseSizeRepository.findById(releaseSizeId))
                .willReturn(Optional.of(releaseSize));
        given(releaseRepository.findById(releaseSize.getReleaseId()))
                .willReturn(Optional.of(release));
        given(releaseSizeService.decreaseStockWithLock(releaseSizeId, 1))
                .willReturn(releaseSize);
        given(orderRepository.save(any(Order.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(memberId, releaseSizeId);

        assertThat(result.getMemberId()).isEqualTo(memberId);
        assertThat(result.getReleaseSizeId()).isEqualTo(releaseSizeId);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.COMPLETED);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getOrderedAt()).isNotNull();
        then(releaseSizeService).should().decreaseStockWithLock(releaseSizeId, 1);
    }

    @Test
    @DisplayName("중복 주문이면 예외가 발생한다")
    void shouldThrowWhenDuplicateOrder() {
        Long memberId = 2L;
        Long releaseSizeId = 20L;
        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(true);

        assertThatThrownBy(() -> orderService.createOrder(memberId, releaseSizeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DUPLICATE_ORDER");
    }

    @Test
    @DisplayName("사이즈별 재고가 없으면 예외가 발생한다")
    void shouldThrowWhenReleaseSizeMissing() {
        Long memberId = 3L;
        Long releaseSizeId = 30L;
        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(false);
        given(releaseSizeRepository.findById(releaseSizeId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(memberId, releaseSizeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사이즈별 재고를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("발매가 없으면 예외가 발생한다")
    void shouldThrowWhenReleaseMissing() {
        Long memberId = 4L;
        Long releaseSizeId = 40L;
        ReleaseSize releaseSize = releaseSize(50L);
        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(false);
        given(releaseSizeRepository.findById(releaseSizeId))
                .willReturn(Optional.of(releaseSize));
        given(releaseRepository.findById(releaseSize.getReleaseId()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(memberId, releaseSizeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("발매를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("발매 시작 전이면 주문이 차단된다")
    void shouldBlockBeforeRelease() {
        Long memberId = 5L;
        Long releaseSizeId = 60L;
        ReleaseSize releaseSize = releaseSize(70L);
        Release release = Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.now().plusMinutes(5))
                .endDateTime(LocalDateTime.now().plusMinutes(10))
                .totalStock(10)
                .build();
        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(false);
        given(releaseSizeRepository.findById(releaseSizeId))
                .willReturn(Optional.of(releaseSize));
        given(releaseRepository.findById(releaseSize.getReleaseId()))
                .willReturn(Optional.of(release));

        assertThatThrownBy(() -> orderService.createOrder(memberId, releaseSizeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RELEASE_NOT_STARTED");
    }

    @Test
    @DisplayName("발매 종료 후이면 주문이 차단된다")
    void shouldBlockAfterReleaseEnded() {
        Long memberId = 6L;
        Long releaseSizeId = 80L;
        ReleaseSize releaseSize = releaseSize(90L);
        Release release = Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.now().minusMinutes(10))
                .endDateTime(LocalDateTime.now().minusMinutes(1))
                .totalStock(10)
                .build();
        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(false);
        given(releaseSizeRepository.findById(releaseSizeId))
                .willReturn(Optional.of(releaseSize));
        given(releaseRepository.findById(releaseSize.getReleaseId()))
                .willReturn(Optional.of(release));

        assertThatThrownBy(() -> orderService.createOrder(memberId, releaseSizeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RELEASE_ENDED");
    }

    @Test
    @DisplayName("저장 중 중복 예외가 발생하면 DUPLICATE_ORDER로 변환한다")
    void shouldConvertDuplicateOnSave() {
        Long memberId = 7L;
        Long releaseSizeId = 100L;
        ReleaseSize releaseSize = releaseSize(110L);
        Release release = activeRelease();
        given(orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId))
                .willReturn(false);
        given(releaseSizeRepository.findById(releaseSizeId))
                .willReturn(Optional.of(releaseSize));
        given(releaseRepository.findById(releaseSize.getReleaseId()))
                .willReturn(Optional.of(release));
        given(releaseSizeService.decreaseStockWithLock(releaseSizeId, 1))
                .willReturn(releaseSize);
        given(orderRepository.save(any(Order.class)))
                .willThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> orderService.createOrder(memberId, releaseSizeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DUPLICATE_ORDER");
    }

    private ReleaseSize releaseSize(Long releaseId) {
        return ReleaseSize.builder()
                .releaseId(releaseId)
                .size(270)
                .stock(5)
                .price(300000)
                .build();
    }

    private Release activeRelease() {
        return Release.builder()
                .shoeId(1L)
                .releaseDateTime(LocalDateTime.now().minusMinutes(1))
                .endDateTime(LocalDateTime.now().plusMinutes(5))
                .totalStock(10)
                .build();
    }
}
