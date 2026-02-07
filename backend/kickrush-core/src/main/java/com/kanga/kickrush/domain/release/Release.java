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
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private int totalStock;

    @Builder
    public Release(Long shoeId, LocalDateTime releaseDateTime, LocalDateTime endDateTime, int totalStock) {
        if (endDateTime.isBefore(releaseDateTime)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간 이후여야 합니다.");
        }
        this.shoeId = shoeId;
        this.releaseDateTime = releaseDateTime;
        this.endDateTime = endDateTime;
        this.totalStock = totalStock;
    }
}
