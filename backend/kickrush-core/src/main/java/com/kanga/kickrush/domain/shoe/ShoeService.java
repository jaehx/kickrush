package com.kanga.kickrush.domain.shoe;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoeService {
    private final ShoeRepository shoeRepository;

    public ShoeService(ShoeRepository shoeRepository) {
        this.shoeRepository = shoeRepository;
    }

    @Transactional(readOnly = true)
    public Shoe findById(Long id) {
        return shoeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public List<Shoe> findAll() {
        return shoeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Shoe> findByBrand(String brand) {
        return shoeRepository.findByBrandIgnoreCase(brand);
    }

    @Transactional
    public Shoe create(String name,
            String brand,
            String modelNumber,
            int price,
            String description,
            String imageUrl) {
        Shoe shoe = Shoe.builder()
            .name(name)
            .brand(brand)
            .modelNumber(modelNumber)
            .price(price)
            .description(description)
            .imageUrl(imageUrl)
            .build();
        return shoeRepository.save(shoe);
    }
}
