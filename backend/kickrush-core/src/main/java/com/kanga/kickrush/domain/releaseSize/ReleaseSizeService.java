package com.kanga.kickrush.domain.releaseSize;

import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseSizeService {
    private final ReleaseSizeRepository releaseSizeRepository;

    public ReleaseSizeService(ReleaseSizeRepository releaseSizeRepository) {
        this.releaseSizeRepository = releaseSizeRepository;
    }

    @Transactional(readOnly = true)
    public ReleaseSize findById(Long id) {
        return releaseSizeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사이즈별 재고를 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public int getStock(Long id) {
        return findById(id).getStock();
    }

    @Transactional
    public ReleaseSize decreaseStock(Long id, int quantity) {
        ReleaseSize releaseSize = findById(id);
        releaseSize.decreaseStock(quantity);
        return releaseSize;
    }

    @Transactional
    public ReleaseSize decreaseStockWithLock(Long id, int quantity) {
        try {
            ReleaseSize releaseSize = releaseSizeRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("사이즈별 재고를 찾을 수 없습니다"));
            releaseSize.decreaseStock(quantity);
            return releaseSize;
        } catch (PessimisticLockException | LockTimeoutException ex) {
            throw new IllegalStateException("LOCK_TIMEOUT", ex);
        }
    }
}
