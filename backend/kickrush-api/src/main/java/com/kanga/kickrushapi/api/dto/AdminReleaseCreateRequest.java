package com.kanga.kickrushapi.api.dto;

import java.util.List;

public record AdminReleaseCreateRequest(Long shoeId, String releaseDateTime, String endDateTime,
                                        List<AdminReleaseSizeRequest> sizes) {
}
