package com.meyame.timemachine.controller;

import com.meyame.timemachine.dto.request.timemachine.TimeMachineCreateReqDto;
import com.meyame.timemachine.dto.request.timemachine.TimeMachineUpdateReqDto;
import com.meyame.timemachine.dto.response.timemachine.TimeMachineInfoResDto;
import com.meyame.timemachine.jwt.UserPrincipal;
import com.meyame.timemachine.repository.TimeMachineRepository;
import com.meyame.timemachine.service.TimeMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 기본적으로 모두 로그인 한 사용자만 접근 가능
@RestController
@RequestMapping("/api/time-machines")
@RequiredArgsConstructor
public class TimeMachineController {

    private final TimeMachineService timeMachineService;
    private final TimeMachineRepository timeMachineRepository;

    // 타임머신 생성
    @PostMapping
    public ResponseEntity<TimeMachineInfoResDto> createTimeMachine(@RequestBody TimeMachineCreateReqDto timeMachineCreateReqDto,@AuthenticationPrincipal UserPrincipal userPrincipal ) {
        TimeMachineInfoResDto res = timeMachineService.createTimeMachine(timeMachineCreateReqDto, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 자신의 특정 타임머신 조회
    @GetMapping("/{timeMachineId}")
    public ResponseEntity<TimeMachineInfoResDto> getUserTimeMachine(@PathVariable Long timeMachineId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        TimeMachineInfoResDto  res = timeMachineService.getUserTimeMachine(timeMachineId, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // 자신의 모든 타임머신 조회
    @GetMapping
    public ResponseEntity<List<TimeMachineInfoResDto>> getAllUserTimeMachinesInfo(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<TimeMachineInfoResDto> timeMachineList = timeMachineService.getAllUserTimeMachinesInfo(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(timeMachineList);
    }

    // 타임머신 수정
    @PatchMapping("/{timeMachineId}")
    public ResponseEntity<TimeMachineInfoResDto> updateTimeMachine(@RequestBody TimeMachineUpdateReqDto timeMachineUpdateReqDto, @PathVariable("timeMachineId") Long timeMachineId, @AuthenticationPrincipal UserPrincipal userPrincipal){
        TimeMachineInfoResDto res = timeMachineService.updateTimeMachine(timeMachineUpdateReqDto, timeMachineId, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // 타임머신 삭제
    @DeleteMapping("/{timeMachineId}")
    public ResponseEntity<?> deleteTimeMachine(@PathVariable("timeMachineId") Long timeMachineId, @AuthenticationPrincipal UserPrincipal userPrincipal){
        timeMachineService.deleteTimeMachine(timeMachineId, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
