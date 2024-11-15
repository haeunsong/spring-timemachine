package com.meyame.timemachine.dto.response.timemachine;

import com.meyame.timemachine.domain.TimeMachine;
import lombok.Builder;

import java.util.Date;

@Builder
public record TimeMachineInfoResDto(
        Long id,
        String name,
        String message,
        Date departureDate,
        Date targetDate
){
    public static TimeMachineInfoResDto from(TimeMachine timeMachine) {
        return TimeMachineInfoResDto.builder()
                .id(timeMachine.getId())
                .name(timeMachine.getName())
                .message(timeMachine.getMessage())
                .departureDate(timeMachine.getDepartureDate())
                .targetDate(timeMachine.getTargetDate())
                .build();
    }
}
