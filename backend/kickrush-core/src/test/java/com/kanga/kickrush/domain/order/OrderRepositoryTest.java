package com.kanga.kickrush.domain.order;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문을 저장할 수 있다")
    void shouldSaveOrder() {
        Order order = Order.builder()
                .memberId(1L)
                .releaseSizeId(2L)
                .status(OrderStatus.COMPLETED)
                .orderedAt(LocalDateTime.now())
                .build();

        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMemberId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("ID로 주문을 조회할 수 있다")
    void shouldFindOrderById() {
        Order order = Order.builder()
                .memberId(2L)
                .releaseSizeId(3L)
                .status(OrderStatus.COMPLETED)
                .orderedAt(LocalDateTime.now())
                .build();
        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getMemberId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("주문을 삭제할 수 있다")
    void shouldDeleteOrder() {
        Order order = Order.builder()
                .memberId(3L)
                .releaseSizeId(4L)
                .status(OrderStatus.COMPLETED)
                .orderedAt(LocalDateTime.now())
                .build();
        Order saved = orderRepository.save(order);

        orderRepository.deleteById(saved.getId());

        Optional<Order> deleted = orderRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }
}
