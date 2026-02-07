package com.kanga.kickrushapi.api.dto;

public record AdminShoeCreateRequest(String name, String brand, String modelNumber, int price,
                                     String description, String imageUrl) {
}
