package com.kanga.kickrush.domain.releaseSize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "release_sizes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReleaseSize {
    private static final int MIN_SIZE = 220;
    private static final int MAX_SIZE = 300;
    private static final int SIZE_STEP = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long releaseId;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int price;

    @Builder
    public ReleaseSize(Long releaseId, int size, int stock, int price) {
        validateSize(size);
        this.releaseId = releaseId;
        this.size = size;
        this.stock = stock;
        this.price = price;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("차감 수량은 1개 이상이어야 합니다");
        }
        if (stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        this.stock -= quantity;
    }

    private static void validateSize(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE || size % SIZE_STEP != 0) {
            throw new IllegalArgumentException("유효하지 않은 사이즈");
        }
    }
}
