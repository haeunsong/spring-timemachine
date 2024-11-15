package com.meyame.timemachine.repository;

import com.meyame.timemachine.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUserId(Long id);
    RefreshToken findByUserId(Long id);
}
