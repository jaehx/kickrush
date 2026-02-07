package com.kanga.kickrush.domain.releaseSize;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReleaseSizeService {

    private final ReleaseSizeRepository releaseSizeRepository;

    public ReleaseSize findById(Long id) {
        return releaseSizeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사이즈별 재고를 찾을 수 없습니다. ID: " + id));
    }

    public List<ReleaseSize> findAll() {
        return releaseSizeRepository.findAll();
    }

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
                    .orElseThrow(() -> new IllegalArgumentException("사이즈별 재고를 찾을 수 없습니다. ID: " + id));
            releaseSize.decreaseStock(quantity);
            return releaseSize;
        } catch (PessimisticLockException | LockTimeoutException ex) {
            throw new IllegalStateException("LOCK_TIMEOUT", ex);
        }
    }

}
