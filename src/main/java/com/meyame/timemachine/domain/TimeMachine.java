package com.meyame.timemachine.domain;

import com.meyame.timemachine.domain.auth.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class TimeMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 타임머신 이름

    private String message; // targetDate 로 보낼 메시지

    private Date departureDate; // 타임머신이 출발하는 날짜 (오늘 날짜)

    private Date targetDate; // 타임머신이 도착하는 날짜 (사용자 지정)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public TimeMachine(Long id, String name, String message, Date departureDate, Date targetDate, User user) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.departureDate = departureDate;
        this.targetDate = targetDate;
        this.user = user;
    }

    public void update(String name,String message, Date targetDate) {
        this.name = name;
        this.message = message;
        this.targetDate = targetDate;
    }
}
