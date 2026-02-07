package com.kanga.kickrushapi.service;

import com.kanga.kickrushapi.api.dto.ShoeDetailDto;
import com.kanga.kickrushapi.api.dto.ShoeDto;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.MockStore;
import com.kanga.kickrushapi.mock.Shoe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoeQueryService {

    public List<ShoeDto> findAll(String brand) {
        return MockStore.SHOES.stream()
                .filter(shoe -> brand == null || shoe.brand().equalsIgnoreCase(brand))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ShoeDetailDto findDetail(Long id) {
        Shoe shoe = MockStore.findShoe(id)
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        return new ShoeDetailDto(shoe.id(), shoe.name(), shoe.brand(), shoe.modelNumber(), shoe.price(),
                shoe.description(), shoe.imageUrl());
    }

    private ShoeDto toDto(Shoe shoe) {
        return new ShoeDto(shoe.id(), shoe.name(), shoe.brand(), shoe.modelNumber(), shoe.price());
    }
}
