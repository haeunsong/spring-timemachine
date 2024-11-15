package com.meyame.timemachine.service;

import com.meyame.timemachine.domain.auth.RefreshToken;
import com.meyame.timemachine.domain.auth.Role;
import com.meyame.timemachine.dto.request.auth.RefreshTokenSignInReqDto;
import com.meyame.timemachine.dto.request.auth.SignInReqDto;
import com.meyame.timemachine.dto.response.auth.RefreshTokenSignInResDto;
import com.meyame.timemachine.dto.response.auth.AccessTokenResDto;
import com.meyame.timemachine.exception.code.ErrorCode;
import com.meyame.timemachine.exception.CustomException;
import com.meyame.timemachine.domain.auth.User;
import com.meyame.timemachine.dto.request.auth.SignUpReqDto;
import com.meyame.timemachine.dto.response.auth.SignUpResDto;
import com.meyame.timemachine.jwt.JwtTokenProvider;
import com.meyame.timemachine.repository.RefreshTokenRepository;
import com.meyame.timemachine.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    @Transactional
    public SignUpResDto signUp(SignUpReqDto signUpReqDto) {
        // 이메일 중복 체크
        String email = signUpReqDto.email();
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        // 패스워드 암호화
        String password = signUpReqDto.password();
        String encodedPassword = passwordEncoder.encode(password);

        User user = userRepository.save(User.builder()
                .email(signUpReqDto.email())
                .password(encodedPassword)
                .name(signUpReqDto.name())
                .role(Role.USER)
                .build());
        return SignUpResDto.from(user);
    }

    // 로그인
    @Transactional
    public AccessTokenResDto signIn(SignInReqDto signInReqDto) {
        User user = userRepository.findByEmail(signInReqDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 비밀번호 검증
        if(!passwordEncoder.matches(signInReqDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        // access token 생성
        String token = jwtTokenProvider.createAccessToken(user);
        return new AccessTokenResDto(token);
    }

    // refresh token 을 사용한 로그인
    @Transactional
    public RefreshTokenSignInResDto signInWithRefreshToken(RefreshTokenSignInReqDto refreshTokenSignInReqDto) {
        User user = userRepository.findByEmail(refreshTokenSignInReqDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 비밀번호 검증
        if(!passwordEncoder.matches(refreshTokenSignInReqDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // access token + refresh token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        // 기존 refresh token 삭제 + 새롭게 생성 후 저장
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId()) // User 엔티티의 id를 userId로 설정
                .refreshToken(refreshToken)
                .expiration(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenValidityTime()))
                .build());

        return new RefreshTokenSignInResDto(accessToken, refreshToken);
    }

    public AccessTokenResDto reissueAccessToken(String refreshToken) {
        // refresh token 의 유효성 검사
        if(!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        // db에 저장된 refresh token 과 요청에서 가져온 refresh token 이 일치하는지 확인
        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);
        RefreshToken storedRefreshToken = refreshTokenRepository.findByUserId(userId);

        if(!storedRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.INCORRECT_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        return AccessTokenResDto.from(newAccessToken);
    }
}
