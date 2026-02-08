package com.kanga.kickrush.domain.release;

import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeRepository;
import com.kanga.kickrush.domain.shoe.ShoeRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseService {
    private final ReleaseRepository releaseRepository;
    private final ReleaseSizeRepository releaseSizeRepository;
    private final ShoeRepository shoeRepository;

    public ReleaseService(ReleaseRepository releaseRepository,
            ReleaseSizeRepository releaseSizeRepository,
            ShoeRepository shoeRepository) {
        this.releaseRepository = releaseRepository;
        this.releaseSizeRepository = releaseSizeRepository;
        this.shoeRepository = shoeRepository;
    }

    @Transactional(readOnly = true)
    public Release findById(Long id) {
        return releaseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("발매를 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public List<Release> findAll() {
        return releaseRepository.findAll();
    }

    @Transactional
    public Release createRelease(Long shoeId,
            LocalDateTime releaseDateTime,
            LocalDateTime endDateTime,
            List<ReleaseSizeSpec> sizes) {
        if (!shoeRepository.existsById(shoeId)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다");
        }
        int totalStock = sizes.stream().mapToInt(ReleaseSizeSpec::stock).sum();
        Release release = releaseRepository.save(new Release(shoeId, releaseDateTime, endDateTime, totalStock));
        List<ReleaseSize> releaseSizes = sizes.stream()
            .map(size -> new ReleaseSize(release.getId(), size.size(), size.stock(), size.price()))
            .toList();
        releaseSizeRepository.saveAll(releaseSizes);
        return release;
    }
}
