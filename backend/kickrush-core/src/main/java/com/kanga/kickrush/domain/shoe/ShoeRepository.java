package com.kanga.kickrush.domain.shoe;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    List<Shoe> findByBrandIgnoreCase(String brand);
}
