package com.meyame.timemachine.dto.request.user;

public record UserUpdateReqDto(
        String email,
        String password,
        String name
){}
