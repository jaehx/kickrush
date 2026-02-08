package com.kanga.kickrush.domain.release;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseService {
    private final ReleaseRepository releaseRepository;

    public ReleaseService(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Transactional(readOnly = true)
    public Release findById(Long id) {
        return releaseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("발매를 찾을 수 없습니다"));
    }
}
