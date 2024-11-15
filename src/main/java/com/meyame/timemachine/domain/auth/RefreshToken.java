package com.meyame.timemachine.domain.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // User 의 id 필드와 연결

    private String refreshToken;

    private LocalDateTime expiration;

    @Builder
    public RefreshToken(Long userId, String refreshToken, LocalDateTime expiration) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
