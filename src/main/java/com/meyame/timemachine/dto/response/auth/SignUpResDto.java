package com.meyame.timemachine.dto.response.auth;

import com.meyame.timemachine.domain.auth.Role;
import com.meyame.timemachine.domain.auth.User;
import lombok.Builder;

@Builder
public record SignUpResDto(
        Long id,
        String name,
        String email,
        Role role
){
    public static SignUpResDto from(User user) {
        return SignUpResDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
