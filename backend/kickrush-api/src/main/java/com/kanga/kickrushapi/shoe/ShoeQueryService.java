package com.kanga.kickrushapi.shoe;

import com.kanga.kickrush.domain.shoe.Shoe;
import com.kanga.kickrush.domain.shoe.ShoeService;
import com.kanga.kickrushapi.common.ApiException;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.shoe.dto.ShoeDetailDto;
import com.kanga.kickrushapi.shoe.dto.ShoeDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ShoeQueryService {

    private final ShoeService shoeService;

    public ShoeQueryService(ShoeService shoeService) {
        this.shoeService = shoeService;
    }

    public List<ShoeDto> findAll(String brand) {
        List<Shoe> shoes = brand == null ? shoeService.findAll() : shoeService.findByBrand(brand);
        return shoes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ShoeDetailDto findDetail(Long id) {
        try {
            Shoe shoe = shoeService.findById(id);
            return new ShoeDetailDto(shoe.getId(), shoe.getName(), shoe.getBrand(), shoe.getModelNumber(),
                shoe.getPrice(), shoe.getDescription(), shoe.getImageUrl());
        } catch (IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.SHOE_NOT_FOUND, HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");
        }
    }

    private ShoeDto toDto(Shoe shoe) {
        return new ShoeDto(shoe.getId(), shoe.getName(), shoe.getBrand(), shoe.getModelNumber(), shoe.getPrice());
    }
}
