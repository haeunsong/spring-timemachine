package com.meyame.timemachine.dto.request.auth;

public record RefreshTokenSignInReqDto(
        String email,
        String password
){}
