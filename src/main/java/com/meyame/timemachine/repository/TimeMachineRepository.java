package com.meyame.timemachine.repository;

import com.meyame.timemachine.domain.TimeMachine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeMachineRepository extends JpaRepository<TimeMachine, Long> {
    List<TimeMachine> findByUserId(Long id);
}
