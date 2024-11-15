package com.meyame.timemachine.dto.request.timemachine;

import com.meyame.timemachine.domain.TimeMachine;

import java.util.Date;

public record TimeMachineCreateReqDto (
        String name,
        String message,
        Date departureDate,
        Date targetDate
){
    public TimeMachine toEntity() {
        return TimeMachine.builder()
                .name(name)
                .message(message)
                .departureDate(departureDate)
                .targetDate(targetDate)
                .build();
    }
}
