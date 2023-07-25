package com.example.jejuairbnb.domain;

import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "email")
    private String email;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    @Column(name = "kakao_auth_id")
    private String kakaoAuthId;

    @Builder
    public User(
            String username,
            String email,
            ProviderEnum provider,
            String kakaoAuthId
    ) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.kakaoAuthId = kakaoAuthId;
    }
}


