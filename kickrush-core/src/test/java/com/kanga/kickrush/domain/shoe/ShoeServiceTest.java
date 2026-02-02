package com.kanga.kickrush.domain.shoe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShoeServiceTest {

    @Mock
    private ShoeRepository shoeRepository;

    @InjectMocks
    private ShoeService shoeService;

    @Test
    @DisplayName("ID로 상품을 조회할 수 있다")
    void shouldFindShoeById() {
        // given
        Long shoeId = 1L;
        Shoe shoe = Shoe.builder()
                .name("Air Jordan 1")
                .brand("Nike")
                .modelNumber("AJ1-001")
                .price(150000)
                .build();
        given(shoeRepository.findById(shoeId)).willReturn(Optional.of(shoe));

        // when
        Shoe foundShoe = shoeService.findById(shoeId);

        // then
        assertThat(foundShoe).isNotNull();
        assertThat(foundShoe.getName()).isEqualTo("Air Jordan 1");
        assertThat(foundShoe.getBrand()).isEqualTo("Nike");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
    void shouldThrowExceptionWhenShoeNotFound() {
        // given
        Long nonExistentId = 999L;
        given(shoeRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> shoeService.findById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("모든 상품을 조회할 수 있다")
    void shouldFindAllShoes() {
        // given
        Shoe shoe1 = Shoe.builder()
                .name("Air Jordan 1")
                .brand("Nike")
                .modelNumber("AJ1-001")
                .price(150000)
                .build();
        Shoe shoe2 = Shoe.builder()
                .name("Yeezy Boost 350")
                .brand("Adidas")
                .modelNumber("YB350-001")
                .price(200000)
                .build();
        given(shoeRepository.findAll()).willReturn(Arrays.asList(shoe1, shoe2));

        // when
        List<Shoe> shoes = shoeService.findAll();

        // then
        assertThat(shoes).hasSize(2);
        assertThat(shoes).extracting("name")
                .containsExactly("Air Jordan 1", "Yeezy Boost 350");
    }
}
