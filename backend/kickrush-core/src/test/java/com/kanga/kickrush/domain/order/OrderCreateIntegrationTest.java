package com.kanga.kickrush.domain.order;

import com.kanga.kickrush.TestConfiguration;
import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseRepository;
import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestConfiguration.class)
class OrderCreateIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseSizeRepository releaseSizeRepository;

    @Test
    @DisplayName("주문 생성 핵심 경로: 재고 차감 + 중복 방지 + 시간 검증")
    void shouldCreateOrderWithValidations() {
        LocalDateTime now = LocalDateTime.now();
        Release release = releaseRepository.save(Release.builder()
                .shoeId(1L)
                .releaseDateTime(now.minusMinutes(1))
                .endDateTime(now.plusMinutes(5))
                .totalStock(10)
                .build());
        ReleaseSize releaseSize = releaseSizeRepository.save(ReleaseSize.builder()
                .releaseId(release.getId())
                .size(270)
                .stock(1)
                .price(300000)
                .build());

        Order order = orderService.createOrder(10L, releaseSize.getId());

        assertThat(order.getId()).isNotNull();
        assertThat(order.getMemberId()).isEqualTo(10L);
        assertThat(order.getReleaseSizeId()).isEqualTo(releaseSize.getId());
        assertThat(releaseSizeRepository.findById(releaseSize.getId()).orElseThrow().getStock()).isEqualTo(0);

        assertThatThrownBy(() -> orderService.createOrder(10L, releaseSize.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DUPLICATE_ORDER");
    }

    @Test
    @DisplayName("발매 시작 전이면 주문이 차단된다")
    void shouldBlockOrderBeforeRelease() {
        LocalDateTime now = LocalDateTime.now();
        Release release = releaseRepository.save(Release.builder()
                .shoeId(1L)
                .releaseDateTime(now.plusMinutes(5))
                .endDateTime(now.plusMinutes(10))
                .totalStock(10)
                .build());
        ReleaseSize releaseSize = releaseSizeRepository.save(ReleaseSize.builder()
                .releaseId(release.getId())
                .size(265)
                .stock(3)
                .price(290000)
                .build());

        assertThatThrownBy(() -> orderService.createOrder(1L, releaseSize.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RELEASE_NOT_STARTED");
    }

    @Test
    @DisplayName("발매 종료 후이면 주문이 차단된다")
    void shouldBlockOrderAfterReleaseEnded() {
        LocalDateTime now = LocalDateTime.now();
        Release release = releaseRepository.save(Release.builder()
                .shoeId(1L)
                .releaseDateTime(now.minusMinutes(10))
                .endDateTime(now.minusMinutes(1))
                .totalStock(10)
                .build());
        ReleaseSize releaseSize = releaseSizeRepository.save(ReleaseSize.builder()
                .releaseId(release.getId())
                .size(260)
                .stock(3)
                .price(280000)
                .build());

        assertThatThrownBy(() -> orderService.createOrder(2L, releaseSize.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RELEASE_ENDED");
    }
}
