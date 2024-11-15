package com.meyame.timemachine.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meyame.timemachine.domain.TimeMachine;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private Role role;

    @JsonIgnore // 순환 참조 방지
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<TimeMachine> timeMachineList = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = Role.USER;
    }

    public void update(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
