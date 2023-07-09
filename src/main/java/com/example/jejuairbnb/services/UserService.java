package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.UserControllerRequestDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@AllArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    @Transactional
    public CreateUserResponseDto registerUser(
            UserControllerRequestDto requestDto
    ) throws NoSuchAlgorithmException {
        System.out.println("회원가입 요청: " + requestDto);
        User foundUser = userRepository.findByEmail(requestDto.getEmail());
        if (foundUser != null) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }

        String password = requestDto.getPassword();

        if (!password.equals(requestDto.getRePassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        String hasingPassword = Base64.getEncoder().encodeToString(hash);

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(hasingPassword)
                .email(requestDto.getEmail())
                .build();

        User savedUser = userRepository.save(user);

        return CreateUserResponseDto.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }
}
