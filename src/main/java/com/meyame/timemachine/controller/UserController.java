package com.meyame.timemachine.controller;

import com.meyame.timemachine.dto.request.user.UserUpdateReqDto;
import com.meyame.timemachine.dto.response.user.UserInfoResDto;
import com.meyame.timemachine.dto.response.user.UserUpdateResDto;
import com.meyame.timemachine.jwt.UserPrincipal;
import com.meyame.timemachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 정보 조회 - 로그인한 사용자가 자신의 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<UserInfoResDto> getUserInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserInfoResDto res = userService.getUserInfo(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // 유저 정보 수정
    @PatchMapping("/profile")
    public ResponseEntity<UserUpdateResDto> updateUserInfo(@RequestBody UserUpdateReqDto userUpdateReqDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserUpdateResDto res = userService.updateUserInfo(userUpdateReqDto, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // 유저 탈퇴
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteUser(userPrincipal.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
