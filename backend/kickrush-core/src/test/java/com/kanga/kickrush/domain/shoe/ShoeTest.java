package com.kanga.kickrush.domain.shoe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShoeTest {

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void shouldCreateShoe() {
        // given
        String name = "Air Jordan 1";
        String brand = "Nike";
        String modelNumber = "AJ1-001";
        int price = 150000;

        // when
        Shoe shoe = Shoe.builder()
                .name(name)
                .brand(brand)
                .modelNumber(modelNumber)
                .price(price)
                .build();

        // then
        assertThat(shoe).isNotNull();
        assertThat(shoe.getName()).isEqualTo(name);
        assertThat(shoe.getBrand()).isEqualTo(brand);
        assertThat(shoe.getModelNumber()).isEqualTo(modelNumber);
        assertThat(shoe.getPrice()).isEqualTo(price);
    }
}
