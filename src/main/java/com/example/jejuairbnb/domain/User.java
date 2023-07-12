package com.example.jejuairbnb.domain;

import com.example.jejuairbnb.shared.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "kakao_auth_id")
    private String kakaoAuthId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public User(
            String username,
            String password,
            String email,
            String kakaoAuthId
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.kakaoAuthId = kakaoAuthId;
    }
}
