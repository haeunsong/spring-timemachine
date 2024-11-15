package com.meyame.timemachine.service;

import com.meyame.timemachine.domain.TimeMachine;
import com.meyame.timemachine.domain.auth.User;
import com.meyame.timemachine.dto.request.timemachine.TimeMachineCreateReqDto;
import com.meyame.timemachine.dto.request.timemachine.TimeMachineUpdateReqDto;
import com.meyame.timemachine.dto.response.timemachine.TimeMachineInfoResDto;
import com.meyame.timemachine.exception.CustomException;
import com.meyame.timemachine.exception.code.ErrorCode;
import com.meyame.timemachine.repository.TimeMachineRepository;
import com.meyame.timemachine.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeMachineService {

    private final UserRepository userRepository;
    private final TimeMachineRepository timeMachineRepository;

    @Transactional
    public TimeMachineInfoResDto createTimeMachine(TimeMachineCreateReqDto timeMachineCreateReqDto, Long userId) {
        // 사용자 ID를 통해 User 엔티티를 조회한다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // TimeMachine 객체를 생성하고 로그인한 사용자를 소유자로 설정한다.
        TimeMachine timeMachine = TimeMachine.builder()
                .name(timeMachineCreateReqDto.name())
                .message(timeMachineCreateReqDto.message())
                .departureDate(new Date()) // 현재 날짜로 지정
                .targetDate(timeMachineCreateReqDto.targetDate())
                .user(user)
                .build();

        timeMachineRepository.save(timeMachine);

        return TimeMachineInfoResDto.from(timeMachine);
    }

    @Transactional(readOnly = true)
    public TimeMachineInfoResDto getUserTimeMachine(Long timeMachineId, Long id) {
        // timeMachineId로 해당 타임머신이 있는지 먼저 조회
        TimeMachine timeMachine = timeMachineRepository.findById(timeMachineId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIMEMACHINE_NOT_FOUND));

        // 해당 타임머신의 소유자가 현재 사용자 id와 일치하는지 확인한다.
        if(!timeMachine.getUser().getId().equals(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        return TimeMachineInfoResDto.from(timeMachine);
    }

    @Transactional(readOnly = true)
    public List<TimeMachineInfoResDto> getAllUserTimeMachinesInfo(Long id) {
        // 사용자 ID 로 모든 타임머신 조회
        List<TimeMachine> timeMachineList = timeMachineRepository.findByUserId(id);

        return timeMachineList.stream()
                .map(TimeMachineInfoResDto::from)
                .toList();
    }

    @Transactional
    public TimeMachineInfoResDto updateTimeMachine(TimeMachineUpdateReqDto timeMachineUpdateReqDto, Long timeMachineId, Long id) {
        // timeMachineId를 통해 수정하려는 타임머신을 조회한다.
        TimeMachine timeMachine = timeMachineRepository.findById(timeMachineId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIMEMACHINE_NOT_FOUND));

        // 해당 타임머신의 소유자가 현재 사용자 id와 일치하는지 확인한다.
        if(!timeMachine.getUser().getId().equals(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        timeMachine.update(timeMachineUpdateReqDto.name(), timeMachineUpdateReqDto.message(), timeMachineUpdateReqDto.targetDate());

        return TimeMachineInfoResDto.from(timeMachine);
    }

    @Transactional
    public void deleteTimeMachine(Long timeMachineId, Long id) {
        TimeMachine timeMachine = timeMachineRepository.findById(timeMachineId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIMEMACHINE_NOT_FOUND));

        // 해당 타임머신의 소유자가 현재 사용자 id와 일치하는지 확인한다.
        if(!timeMachine.getUser().getId().equals(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        timeMachineRepository.delete(timeMachine);
    }
}
