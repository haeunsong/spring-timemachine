package com.meyame.timemachine.jwt;

import com.meyame.timemachine.domain.auth.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String password; // 이미 암호화된 비밀번호를 가지고 있다.
    private final Collection<? extends GrantedAuthority> authorities;

    // User 객체로부터 UserPrincipal 생성
    public static UserPrincipal from(User user) {
        // "ROLE_USER" 권한을 가진 객체를 생성한다.
        // 이 권한 목록은 이후 UserDetails 객체의 authorities 필드에 설정된다.
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
