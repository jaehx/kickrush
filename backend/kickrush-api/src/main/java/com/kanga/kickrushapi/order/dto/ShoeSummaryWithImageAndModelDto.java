package com.kanga.kickrushapi.order.dto;

public record ShoeSummaryWithImageAndModelDto(Long id, String name, String brand, String modelNumber,
                                              String imageUrl) {
}
