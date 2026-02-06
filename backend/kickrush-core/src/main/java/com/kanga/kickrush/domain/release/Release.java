package com.kanga.kickrush.domain.release;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long shoeId;

    @Column(nullable = false)
    private LocalDateTime releaseDateTime;

    @Column(nullable = false)
    private int totalStock;

    @Builder
    public Release(Long shoeId, LocalDateTime releaseDateTime, int totalStock) {
        this.shoeId = shoeId;
        this.releaseDateTime = releaseDateTime;
        this.totalStock = totalStock;
    }
}
