package com.meyame.timemachine.dto.request.timemachine;

import java.util.Date;

public record TimeMachineUpdateReqDto(
        String name,
        String message,
        Date departureDate,
        Date targetDate
){}
