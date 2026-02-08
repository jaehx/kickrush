package com.kanga.kickrush.domain.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByMemberIdAndReleaseSizeId(Long memberId, Long releaseSizeId);

    List<Order> findByMemberIdOrderByOrderedAtDesc(Long memberId);

    Optional<Order> findByIdAndMemberId(Long id, Long memberId);
}
