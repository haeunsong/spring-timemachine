package com.meyame.timemachine.dto.response.user;

import com.meyame.timemachine.domain.auth.Role;
import com.meyame.timemachine.domain.auth.User;
import lombok.Builder;

@Builder
public record UserInfoResDto(
        String email,
        String name,
        Role role
){
    public static UserInfoResDto from(User user) {
        return UserInfoResDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(Role.USER)
                .build();
    }
}
