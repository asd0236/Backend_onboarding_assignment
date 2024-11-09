package com.sanghun.assignment.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // 사용자 ID, Primary Key

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "authorities", nullable = false)
    private Authorities authorities;

    public User(String username, String password, String nickname, Authorities authorities) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.authorities = authorities;
    }

}
