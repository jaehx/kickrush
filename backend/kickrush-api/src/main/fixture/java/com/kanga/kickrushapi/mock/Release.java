package com.kanga.kickrushapi.mock;

import java.time.LocalDateTime;
import java.util.List;

public record Release(Long id, Long shoeId, LocalDateTime releaseDateTime, LocalDateTime endDateTime,
                      ReleaseStatus status, int totalStock, List<ReleaseSize> sizes) {

    public static Release sample(Long id, Long shoeId, LocalDateTime releaseDateTime, LocalDateTime endDateTime,
                                 ReleaseStatus status, int totalStock, List<ReleaseSize> sizes) {
        return new Release(id, shoeId, releaseDateTime, endDateTime, status, totalStock, sizes);
    }
}
