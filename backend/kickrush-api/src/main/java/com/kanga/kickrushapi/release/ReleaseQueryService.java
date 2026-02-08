package com.kanga.kickrushapi.release;

import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseService;
import com.kanga.kickrush.domain.release.ReleaseStatus;
import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.shoe.Shoe;
import com.kanga.kickrush.domain.shoe.ShoeService;
import com.kanga.kickrushapi.common.ApiException;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.release.dto.ReleaseDetailDto;
import com.kanga.kickrushapi.release.dto.ReleaseDto;
import com.kanga.kickrushapi.release.dto.ReleaseSizeDto;
import com.kanga.kickrushapi.shoe.dto.ShoeDetailDto;
import com.kanga.kickrushapi.shoe.dto.ShoeSummaryDto;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseQueryService {

    private final ReleaseService releaseService;
    private final ShoeService shoeService;
    private final Clock clock;

    public ReleaseQueryService(ReleaseService releaseService, ShoeService shoeService, Clock clock) {
        this.releaseService = releaseService;
        this.shoeService = shoeService;
        this.clock = clock;
    }

    public List<ReleaseDto> findAll(String status) {
        LocalDateTime now = LocalDateTime.now(clock);
        return releaseService.findAll().stream()
                .filter(release -> status == null || release.getStatus(now).name().equalsIgnoreCase(status))
                .map(release -> toDto(release, now))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReleaseDetailDto findDetail(Long id) {
        LocalDateTime now = LocalDateTime.now(clock);
        Release release = findRelease(id);
        Shoe shoe = findShoe(release.getShoeId());
        ShoeDetailDto shoeDto = new ShoeDetailDto(shoe.getId(), shoe.getName(), shoe.getBrand(),
            shoe.getModelNumber(), shoe.getPrice(), shoe.getDescription(), shoe.getImageUrl());
        List<ReleaseSizeDto> sizes = release.getSizes().stream()
                .map(this::toSizeDto)
                .collect(Collectors.toList());
        return new ReleaseDetailDto(release.getId(), shoeDto, release.getReleaseDateTime().toString(),
                release.getEndDateTime().toString(), release.getStatus(now).name(), release.getTotalStock(), sizes);
    }

    private ReleaseDto toDto(Release release, LocalDateTime now) {
        Shoe shoe = findShoe(release.getShoeId());
        ShoeSummaryDto shoeSummary = new ShoeSummaryDto(shoe.getId(), shoe.getName(), shoe.getBrand(),
                shoe.getModelNumber(), shoe.getImageUrl());
        return new ReleaseDto(release.getId(), shoeSummary, release.getReleaseDateTime().toString(),
                release.getEndDateTime().toString(), release.getStatus(now).name(), release.getTotalStock());
    }

    private ReleaseSizeDto toSizeDto(ReleaseSize size) {
        return new ReleaseSizeDto(size.getId(), size.getSize(), size.getStock(), size.getPrice());
    }

    private Release findRelease(Long id) {
        try {
            return releaseService.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.RELEASE_NOT_FOUND, HttpStatus.NOT_FOUND, "발매 정보를 찾을 수 없습니다.");
        }
    }

    private Shoe findShoe(Long id) {
        try {
            return shoeService.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.SHOE_NOT_FOUND, HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");
        }
    }
}
