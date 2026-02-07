package com.kanga.kickrush.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByMemberIdAndReleaseSizeId(Long memberId, Long releaseSizeId);
}
