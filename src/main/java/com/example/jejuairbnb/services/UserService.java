package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.FindUserDto.FindUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.UpdateUserDto.UpdateUserRequestDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    static final String DUPLICATE_EMAIL = "중복된 이메일이 존재합니다.";
    static final String INVALID_PASSWORD = "비밀번호가 일치하지 않습니다.";

    static final String NOT_FOUND_USER = "존재하지 않는 유저입니다.";

    private final IUserRepository userRepository;
    @Transactional
    public CreateUserResponseDto registerUser(
            CreateUserRequestDto requestDto
    ) throws NoSuchAlgorithmException {
        System.out.println("회원가입 요청: " + requestDto);
        User foundUser = userRepository.findByEmail(requestDto.getEmail());
        if (foundUser != null) {
            throw new IllegalArgumentException(DUPLICATE_EMAIL);
        }

        String password = requestDto.getPassword();

        if (!password.equals(requestDto.getRePassword())) {
            throw new IllegalArgumentException(INVALID_PASSWORD);
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

    @Transactional
    public FindUserResponseDto findUserById(
           Long userId
    ) {
//      리팩토링 진행해주세요
        Optional<User> findUser = userRepository.findById(userId);

        if (findUser.isPresent()) {
            User user = findUser.get();
            return FindUserResponseDto.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();
        } else {
            throw new IllegalArgumentException(NOT_FOUND_USER);
        }
    }

    @Transactional
    public FindUserResponseDto updateUser(
            UpdateUserRequestDto requestDto
    ) {
        User findUser = userRepository.findByEmail(requestDto.getEmail());

        if (findUser != null) {
            findUser.setUsername(requestDto.getUsername());
            findUser.setEmail(requestDto.getEmail());
            userRepository.save(findUser);
            return FindUserResponseDto.builder()
                    .userId(findUser.getId())
                    .email(findUser.getEmail())
                    .username(findUser.getUsername())
                    .build();
        } else {
            throw new IllegalArgumentException(NOT_FOUND_USER);
        }
    }
}
