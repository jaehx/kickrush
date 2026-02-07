package com.kanga.kickrush.domain.releaseSize;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReleaseSizeRepository extends JpaRepository<ReleaseSize, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select rs from ReleaseSize rs where rs.id = :id")
    Optional<ReleaseSize> findByIdForUpdate(@Param("id") Long id);
}
