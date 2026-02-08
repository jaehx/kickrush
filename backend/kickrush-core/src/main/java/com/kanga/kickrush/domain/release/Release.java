package com.kanga.kickrush.domain.release;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "releases")
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
