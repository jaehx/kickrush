package com.kanga.kickrush.domain.order;

import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseRepository;
import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeService;
import java.time.LocalDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ReleaseRepository releaseRepository;
    private final ReleaseSizeRepository releaseSizeRepository;
    private final ReleaseSizeService releaseSizeService;

    public OrderService(OrderRepository orderRepository,
            ReleaseRepository releaseRepository,
            ReleaseSizeRepository releaseSizeRepository,
            ReleaseSizeService releaseSizeService) {
        this.orderRepository = orderRepository;
        this.releaseRepository = releaseRepository;
        this.releaseSizeRepository = releaseSizeRepository;
        this.releaseSizeService = releaseSizeService;
    }

    @Transactional
    public Order createOrder(Long memberId, Long releaseSizeId) {
        if (orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId)) {
            throw new IllegalStateException("DUPLICATE_ORDER");
        }

        ReleaseSize releaseSize = releaseSizeRepository.findById(releaseSizeId)
            .orElseThrow(() -> new IllegalArgumentException("사이즈별 재고를 찾을 수 없습니다"));

        Release release = releaseRepository.findById(releaseSize.getReleaseId())
            .orElseThrow(() -> new IllegalArgumentException("발매를 찾을 수 없습니다"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(release.getReleaseDateTime())) {
            throw new IllegalStateException("RELEASE_NOT_STARTED");
        }
        if (now.isAfter(release.getEndDateTime())) {
            throw new IllegalStateException("RELEASE_ENDED");
        }

        releaseSizeService.decreaseStockWithLock(releaseSizeId, 1);

        Order order = Order.builder()
            .memberId(memberId)
            .releaseSizeId(releaseSizeId)
            .status(OrderStatus.COMPLETED)
            .orderedAt(now)
            .build();

        try {
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("DUPLICATE_ORDER", ex);
        }
    }
}
