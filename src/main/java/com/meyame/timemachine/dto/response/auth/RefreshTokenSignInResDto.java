package com.meyame.timemachine.dto.response.auth;

import lombok.Builder;

@Builder
public record RefreshTokenSignInResDto(
        String accessToken,
        String refreshToken
){}
