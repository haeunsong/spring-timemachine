package com.meyame.timemachine.dto.request.auth;

public record SignUpReqDto(
        String email,
        String password,
        String name
){}
