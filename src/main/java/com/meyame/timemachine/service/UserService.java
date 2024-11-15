package com.meyame.timemachine.service;

import com.meyame.timemachine.domain.auth.User;
import com.meyame.timemachine.dto.request.user.UserUpdateReqDto;
import com.meyame.timemachine.dto.response.user.UserInfoResDto;
import com.meyame.timemachine.dto.response.user.UserUpdateResDto;
import com.meyame.timemachine.exception.CustomException;
import com.meyame.timemachine.exception.code.ErrorCode;
import com.meyame.timemachine.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResDto getUserInfo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResDto.from(user);
    }

    @Transactional
    public UserUpdateResDto updateUserInfo(UserUpdateReqDto userUpdateReqDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.update(userUpdateReqDto.email(),userUpdateReqDto.password(),userUpdateReqDto.name());

        return UserUpdateResDto.from(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
