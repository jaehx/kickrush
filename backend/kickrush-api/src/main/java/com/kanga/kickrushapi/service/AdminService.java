package com.kanga.kickrushapi.service;

import com.kanga.kickrushapi.api.dto.AdminReleaseCreateRequest;
import com.kanga.kickrushapi.api.dto.AdminReleaseCreateResponse;
import com.kanga.kickrushapi.api.dto.AdminReleaseSizeRequest;
import com.kanga.kickrushapi.api.dto.AdminShoeCreateRequest;
import com.kanga.kickrushapi.api.dto.AdminShoeCreateResponse;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.MockStore;
import com.kanga.kickrushapi.mock.Release;
import com.kanga.kickrushapi.mock.ReleaseSize;
import com.kanga.kickrushapi.mock.ReleaseStatus;
import com.kanga.kickrushapi.mock.Shoe;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    public AdminShoeCreateResponse createShoe(AdminShoeCreateRequest request) {
        long shoeId = MockStore.nextShoeId();
        Shoe shoe = new Shoe(shoeId, request.name(), request.brand(), request.modelNumber(), request.price(),
                request.description(), request.imageUrl());
        MockStore.SHOES.add(shoe);
        return new AdminShoeCreateResponse(shoe.id(), shoe.name(), shoe.brand(), shoe.modelNumber(), shoe.price());
    }

    public AdminReleaseCreateResponse createRelease(AdminReleaseCreateRequest request) {
        MockStore.findShoe(request.shoeId())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        long releaseId = MockStore.nextReleaseId();
        List<ReleaseSize> sizes = request.sizes().stream()
                .map(this::toReleaseSize)
                .toList();
        int totalStock = sizes.stream().mapToInt(ReleaseSize::stock).sum();
        Release release = new Release(releaseId, request.shoeId(), LocalDateTime.parse(request.releaseDateTime()),
                LocalDateTime.parse(request.endDateTime()), ReleaseStatus.UPCOMING, totalStock, sizes);
        MockStore.RELEASES.add(release);
        return new AdminReleaseCreateResponse(release.id(), release.shoeId(), release.releaseDateTime().toString(),
                release.totalStock());
    }

    private ReleaseSize toReleaseSize(AdminReleaseSizeRequest request) {
        long id = MockStore.nextReleaseSizeId();
        return new ReleaseSize(id, request.size(), request.stock(), request.price());
    }
}
