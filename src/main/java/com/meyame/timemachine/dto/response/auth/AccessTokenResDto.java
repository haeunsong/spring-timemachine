package com.meyame.timemachine.dto.response.auth;

import lombok.Builder;

@Builder
public record AccessTokenResDto(
        String accessToken
){
    public static AccessTokenResDto from(String newAcessToken) {
        return AccessTokenResDto.builder()
                .accessToken(newAcessToken)
                .build();
    }
}
