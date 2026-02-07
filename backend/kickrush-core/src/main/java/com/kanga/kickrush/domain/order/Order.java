package com.kanga.kickrush.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_order_member_release_size",
                columnNames = {"memberId", "releaseSizeId"}
        ))
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long releaseSizeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    @Builder
    public Order(Long memberId, Long releaseSizeId, OrderStatus status, LocalDateTime orderedAt) {
        this.memberId = memberId;
        this.releaseSizeId = releaseSizeId;
        this.status = status;
        this.orderedAt = orderedAt;
    }
}
