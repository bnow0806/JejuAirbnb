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
@Table(name = "providers")
public class Provider extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "providername")
    private String providername;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "kakao_auth_id")
    private String kakaoAuthId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Provider(
            String providername,
            String password,
            String email,
            String kakaoAuthId
    ) {
        this.providername = providername;
        this.password = password;
        this.email = email;
        this.kakaoAuthId = kakaoAuthId;
    }
}
