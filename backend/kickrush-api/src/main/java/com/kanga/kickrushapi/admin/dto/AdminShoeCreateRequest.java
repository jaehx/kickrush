package com.kanga.kickrushapi.admin.dto;

public record AdminShoeCreateRequest(String name, String brand, String modelNumber, int price,
                                     String description, String imageUrl) {
}
