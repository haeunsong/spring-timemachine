package com.meyame.timemachine.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements CInterface {

    DUPLICATE_EMAIL(-1,"중복되는 이메일입니다."),
    USER_NOT_FOUND(-2,"사용자를 찾을 수 없습니다."),
    INCORRECT_PASSWORD(-3,"비밀번호가 일치하지 않습니다."),
    TIMEMACHINE_NOT_FOUND(-4, "타임머신을 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS(-5, "해당 타임머신에 접근할 권한이 없습니다."),
    INVALID_REFRESH_TOKEN(-6, "유효하지 않은 Refresh Token 입니다."),
    INCORRECT_REFRESH_TOKEN(-7, "DB에 저장된 Refresh Token 과 일치하지 않습니다.");

    private final int code;
    private final String message;
}
