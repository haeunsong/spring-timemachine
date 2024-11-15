package com.meyame.timemachine.exception;

import com.meyame.timemachine.exception.code.CInterface;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final CInterface cInterface;

    public CustomException(CInterface cInterface) {
        super(cInterface.getMessage());
        this.cInterface = cInterface;
    }

    // 추가적인 예외메시지도 작성하고 싶을 때 사용
    public CustomException(CInterface cInterface, String message) {
        super(cInterface.getMessage() + message);
        this.cInterface = cInterface;
    }
}
