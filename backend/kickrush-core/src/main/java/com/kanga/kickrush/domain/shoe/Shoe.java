package com.kanga.kickrush.domain.shoe;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shoe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false, unique = true)
    private String modelNumber;

    @Column(nullable = false)
    private int price;

    @Builder
    public Shoe(String name, String brand, String modelNumber, int price) {
        this.name = name;
        this.brand = brand;
        this.modelNumber = modelNumber;
        this.price = price;
    }
}
