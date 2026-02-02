package com.kanga.kickrush.domain.shoe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoeService {

    private final ShoeRepository shoeRepository;

    public Shoe findById(Long id) {
        return shoeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
    }

    public List<Shoe> findAll() {
        return shoeRepository.findAll();
    }
}
