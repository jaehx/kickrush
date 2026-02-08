package com.kanga.kickrushapi.shoe.dto;

public record ShoeDetailDto(Long id, String name, String brand, String modelNumber, int price,
                            String description, String imageUrl) {
}
