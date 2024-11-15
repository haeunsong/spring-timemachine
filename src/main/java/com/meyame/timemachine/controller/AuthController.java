package com.meyame.timemachine.controller;

import com.meyame.timemachine.dto.request.auth.RefreshTokenSignInReqDto;
import com.meyame.timemachine.dto.request.auth.SignInReqDto;
import com.meyame.timemachine.dto.request.auth.SignUpReqDto;
import com.meyame.timemachine.dto.response.auth.RefreshTokenSignInResDto;
import com.meyame.timemachine.dto.response.auth.AccessTokenResDto;
import com.meyame.timemachine.dto.response.auth.SignUpResDto;
import com.meyame.timemachine.jwt.JwtTokenProvider;
import com.meyame.timemachine.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @param signUpReqDto 회원가입 요청 DTO
     * @return 성공 시 회원가입 응답 DTO(id, email, name)와 200 OK 반환
     */
    @PostMapping("/signUp")
    public ResponseEntity<SignUpResDto> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        SignUpResDto res = authService.signUp(signUpReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /**
     * @param signInReqDto 로그인 요청 DTO
     * @return 성공 시 JWT 토큰과 200 OK 반환
     */
    @PostMapping("/signIn")
    public ResponseEntity<AccessTokenResDto> signIn(@RequestBody SignInReqDto signInReqDto) {
        AccessTokenResDto token = authService.signIn(signInReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    //refresh token 을 이용한 로그인 요청
    @PostMapping("/signIn/refresh")
    public ResponseEntity<RefreshTokenSignInResDto> refreshTokenSignIn(@RequestBody RefreshTokenSignInReqDto refreshTokenSignInReqDto) {
        RefreshTokenSignInResDto token = authService.signInWithRefreshToken(refreshTokenSignInReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    // access token 만료시 재발급 요청
    // Refresh token 을 헤더에 포함하여 요청
    @PostMapping("/token/reissue")
    public ResponseEntity<AccessTokenResDto> reissueRefreshToken (HttpServletRequest req, HttpServletResponse res) {
        // 요청헤더에서 Refresh Token 추출
        String refreshToken = jwtTokenProvider.resolveToken(req);
        // Refresh Token 을 사용하여 Access Token 재발급
        AccessTokenResDto accessTokenResDto = authService.reissueAccessToken(refreshToken);
        jwtTokenProvider.setAccessTokenHeader(res,accessTokenResDto.accessToken());
        return ResponseEntity.status(HttpStatus.OK).body(accessTokenResDto);
    }
}
