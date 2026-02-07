package com.kanga.kickrush.domain.order;

import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseRepository;
import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReleaseRepository releaseRepository;
    private final ReleaseSizeRepository releaseSizeRepository;

    @Transactional
    public Order createOrder(Long memberId, Long releaseSizeId) {
        try {
            ReleaseSize releaseSize = releaseSizeRepository.findByIdForUpdate(releaseSizeId)
                    .orElseThrow(() -> new IllegalArgumentException("사이즈별 재고를 찾을 수 없습니다. ID: " + releaseSizeId));
            Release release = releaseRepository.findById(releaseSize.getReleaseId())
                    .orElseThrow(() -> new IllegalArgumentException("발매를 찾을 수 없습니다. ID: " + releaseSize.getReleaseId()));

            validateReleaseTime(release);

            if (orderRepository.existsByMemberIdAndReleaseSizeId(memberId, releaseSizeId)) {
                throw new IllegalStateException("DUPLICATE_ORDER");
            }

            releaseSize.decreaseStock(1);
            Order order = Order.builder()
                    .memberId(memberId)
                    .releaseSizeId(releaseSizeId)
                    .status(OrderStatus.COMPLETED)
                    .orderedAt(LocalDateTime.now())
                    .build();
            return orderRepository.save(order);
        } catch (PessimisticLockException | LockTimeoutException ex) {
            throw new IllegalStateException("LOCK_TIMEOUT", ex);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("DUPLICATE_ORDER", ex);
        }
    }

    private void validateReleaseTime(Release release) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(release.getReleaseDateTime())) {
            throw new IllegalStateException("RELEASE_NOT_STARTED");
        }
        if (now.isAfter(release.getEndDateTime())) {
            throw new IllegalStateException("RELEASE_ENDED");
        }
    }
}
