package com.kanga.kickrush.domain.release;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReleaseService {

    private final ReleaseRepository releaseRepository;

    public Release findById(Long id) {
        return releaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("발매를 찾을 수 없습니다. ID: " + id));
    }
}
