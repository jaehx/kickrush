package com.kanga.kickrushapi.service;

import com.kanga.kickrushapi.api.dto.ReleaseDetailDto;
import com.kanga.kickrushapi.api.dto.ReleaseDto;
import com.kanga.kickrushapi.api.dto.ReleaseSizeDto;
import com.kanga.kickrushapi.api.dto.ShoeDetailDto;
import com.kanga.kickrushapi.api.dto.ShoeSummaryDto;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.MockStore;
import com.kanga.kickrushapi.mock.Release;
import com.kanga.kickrushapi.mock.ReleaseSize;
import com.kanga.kickrushapi.mock.ReleaseStatus;
import com.kanga.kickrushapi.mock.Shoe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleaseQueryService {

    public List<ReleaseDto> findAll(String status) {
        return MockStore.RELEASES.stream()
                .filter(release -> status == null || release.status().name().equalsIgnoreCase(status))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ReleaseDetailDto findDetail(Long id) {
        Release release = MockStore.findRelease(id)
                .orElseThrow(() -> MockStore.apiError(ErrorCode.RELEASE_NOT_FOUND, "발매 정보를 찾을 수 없습니다."));
        Shoe shoe = MockStore.findShoe(release.shoeId())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        ShoeDetailDto shoeDto = new ShoeDetailDto(shoe.id(), shoe.name(), shoe.brand(), shoe.modelNumber(),
                shoe.price(), shoe.description(), shoe.imageUrl());
        List<ReleaseSizeDto> sizes = release.sizes().stream()
                .map(this::toSizeDto)
                .collect(Collectors.toList());
        return new ReleaseDetailDto(release.id(), shoeDto, release.releaseDateTime().toString(),
                release.endDateTime().toString(), release.status().name(), release.totalStock(), sizes);
    }

    private ReleaseDto toDto(Release release) {
        Shoe shoe = MockStore.findShoe(release.shoeId())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        ShoeSummaryDto shoeSummary = new ShoeSummaryDto(shoe.id(), shoe.name(), shoe.brand(),
                shoe.modelNumber(), shoe.imageUrl());
        return new ReleaseDto(release.id(), shoeSummary, release.releaseDateTime().toString(),
                release.endDateTime().toString(), release.status().name(), release.totalStock());
    }

    private ReleaseSizeDto toSizeDto(ReleaseSize size) {
        return new ReleaseSizeDto(size.id(), size.size(), size.stock(), size.price());
    }

    public ReleaseStatus resolveStatus(Long releaseId) {
        Release release = MockStore.findRelease(releaseId)
                .orElseThrow(() -> MockStore.apiError(ErrorCode.RELEASE_NOT_FOUND, "발매 정보를 찾을 수 없습니다."));
        return release.status();
    }

    public ReleaseSize findReleaseSize(Long releaseSizeId) {
        return MockStore.findReleaseSize(releaseSizeId)
                .orElseThrow(() -> MockStore.apiError(ErrorCode.RELEASE_NOT_FOUND, "발매 정보를 찾을 수 없습니다."));
    }

    public Release findReleaseBySize(Long releaseSizeId) {
        return MockStore.RELEASES.stream()
                .filter(r -> r.sizes().stream().anyMatch(s -> s.id().equals(releaseSizeId)))
                .findFirst()
                .orElseThrow(() -> MockStore.apiError(ErrorCode.RELEASE_NOT_FOUND, "발매 정보를 찾을 수 없습니다."));
    }
}
