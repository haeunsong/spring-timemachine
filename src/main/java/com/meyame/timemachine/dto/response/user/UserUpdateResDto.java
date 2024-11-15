package com.meyame.timemachine.dto.response.user;

import com.meyame.timemachine.domain.auth.Role;
import com.meyame.timemachine.domain.auth.User;
import lombok.Builder;

@Builder
public record UserUpdateResDto(
        Long id,
        String email,
        String password,
        String name,
        Role role
){
    public static UserUpdateResDto from(User user) {
        return UserUpdateResDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}

