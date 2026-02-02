package com.kanga.kickrush.domain.shoe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShoeRepositoryTest {

    @Autowired
    private ShoeRepository shoeRepository;

    @Test
    @DisplayName("상품을 저장할 수 있다")
    void shouldSaveShoe() {
        // given
        Shoe shoe = Shoe.builder()
                .name("Air Jordan 1")
                .brand("Nike")
                .modelNumber("AJ1-001")
                .price(150000)
                .build();

        // when
        Shoe savedShoe = shoeRepository.save(shoe);

        // then
        assertThat(savedShoe.getId()).isNotNull();
        assertThat(savedShoe.getName()).isEqualTo("Air Jordan 1");
    }

    @Test
    @DisplayName("ID로 상품을 조회할 수 있다")
    void shouldFindShoeById() {
        // given
        Shoe shoe = Shoe.builder()
                .name("Air Jordan 1")
                .brand("Nike")
                .modelNumber("AJ1-001")
                .price(150000)
                .build();
        Shoe savedShoe = shoeRepository.save(shoe);

        // when
        Optional<Shoe> foundShoe = shoeRepository.findById(savedShoe.getId());

        // then
        assertThat(foundShoe).isPresent();
        assertThat(foundShoe.get().getName()).isEqualTo("Air Jordan 1");
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void shouldDeleteShoe() {
        // given
        Shoe shoe = Shoe.builder()
                .name("Air Jordan 1")
                .brand("Nike")
                .modelNumber("AJ1-001")
                .price(150000)
                .build();
        Shoe savedShoe = shoeRepository.save(shoe);

        // when
        shoeRepository.deleteById(savedShoe.getId());

        // then
        Optional<Shoe> deletedShoe = shoeRepository.findById(savedShoe.getId());
        assertThat(deletedShoe).isEmpty();
    }
}
