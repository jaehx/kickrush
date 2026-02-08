package com.kanga.kickrushapi.admin;

import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseService;
import com.kanga.kickrush.domain.release.ReleaseSizeSpec;
import com.kanga.kickrush.domain.shoe.Shoe;
import com.kanga.kickrush.domain.shoe.ShoeService;
import com.kanga.kickrushapi.admin.dto.AdminReleaseCreateRequest;
import com.kanga.kickrushapi.admin.dto.AdminReleaseCreateResponse;
import com.kanga.kickrushapi.admin.dto.AdminShoeCreateRequest;
import com.kanga.kickrushapi.admin.dto.AdminShoeCreateResponse;
import com.kanga.kickrushapi.common.ApiException;
import com.kanga.kickrushapi.common.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final ShoeService shoeService;
    private final ReleaseService releaseService;

    public AdminService(ShoeService shoeService, ReleaseService releaseService) {
        this.shoeService = shoeService;
        this.releaseService = releaseService;
    }

    public AdminShoeCreateResponse createShoe(AdminShoeCreateRequest request) {
        Shoe shoe = shoeService.create(request.name(), request.brand(), request.modelNumber(), request.price(),
            request.description(), request.imageUrl());
        return new AdminShoeCreateResponse(shoe.getId(), shoe.getName(), shoe.getBrand(), shoe.getModelNumber(),
            shoe.getPrice());
    }

    public AdminReleaseCreateResponse createRelease(AdminReleaseCreateRequest request) {
        List<ReleaseSizeSpec> sizes = request.sizes().stream()
            .map(size -> new ReleaseSizeSpec(size.size(), size.stock(), size.price()))
            .toList();
        try {
            Release release = releaseService.createRelease(request.shoeId(),
                LocalDateTime.parse(request.releaseDateTime()),
                LocalDateTime.parse(request.endDateTime()),
                sizes);
            return new AdminReleaseCreateResponse(release.getId(), release.getShoeId(),
                release.getReleaseDateTime().toString(), release.getTotalStock());
        } catch (IllegalArgumentException ex) {
            if ("상품을 찾을 수 없습니다".equals(ex.getMessage())) {
                throw new ApiException(ErrorCode.SHOE_NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage());
            }
            throw new ApiException(ErrorCode.INVALID_PARAMETER, HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
