package com.kanga.kickrush.domain.releaseSize;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
