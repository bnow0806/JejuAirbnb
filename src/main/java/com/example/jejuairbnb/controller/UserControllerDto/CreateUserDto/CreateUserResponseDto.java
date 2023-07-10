package com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto;

import com.example.jejuairbnb.shared.CheckValidity;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateUserResponseDto implements CheckValidity {
    private String username;
    private String password;
    private String email;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    @Override
    public void check() {
        if(email == null || email.isEmpty()) {
            throw new RuntimeException("email is empty");
        }

        if(username == null || username.isEmpty()) {
            throw new RuntimeException("username is empty");
        }

        if(password == null || password.isEmpty()) {
            throw new RuntimeException("password is empty");
        }

        if (password.length() < 8 || password.length() > 16) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 16자 이하로 입력해주세요.");
        }
    }

    @Builder
    public CreateUserResponseDto(
            String username,
            String email,
            String password,
            String createdAt,
            String updatedAt,
            String deletedAt
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
