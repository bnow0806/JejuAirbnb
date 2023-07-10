package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.FindUserDto.FindUserResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import static com.example.jejuairbnb.services.UserService.DUPLICATE_EMAIL;

@SpringBootTest
public class UserServiceTest {

    @MockBean // 스프링 부트 테스트에서 사용하는 목(mock) 객체를 생성하는 어노테이션입니다
    private IUserRepository userRepository;

    private UserService userService;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        userService = new UserService(userRepository);
    }

    @Test
    public void testRegisterUser() throws NoSuchAlgorithmException {
        // given
        CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
                .username("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestDto.getPassword().getBytes(StandardCharsets.UTF_8));

        String hashingPassword = Base64.getEncoder().encodeToString(hash);

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(hashingPassword)
                .email(requestDto.getEmail())
                .build();

        Mockito.when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(null);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        CreateUserResponseDto responseDto = userService.registerUser(requestDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(requestDto.getUsername(), responseDto.getUsername());
        Assertions.assertEquals(requestDto.getEmail(), responseDto.getEmail());
    }

    @Test
    public void testExistEmail() throws NoSuchAlgorithmException {
        // requestDto 으로 일단 기본 셋팅을 한다.
        CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
                .username("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestDto.getPassword().getBytes(StandardCharsets.UTF_8));

        String hashingPassword = Base64.getEncoder().encodeToString(hash);

        User existingUser = User.builder()
                .username(requestDto.getUsername())
                .password(hashingPassword)
                .email(requestDto.getEmail())
                .build();

        Mockito.when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(existingUser);
        // 위에서 mock 객체를 생성했기 때문에, userRepository.findByEmail() 메소드를 호출하면
        // existingUser 객체를 리턴하도록 설정 한다.

        // then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // when
            userService.registerUser(requestDto);
        });
        Assertions.assertEquals(DUPLICATE_EMAIL, exception.getMessage());
    }

    @Test
    public void invalidPasswordAndRePassword() {
        // given
        CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
                .username("test")
                .password("test")
                .rePassword("t")
                .email("test@gmail.com")
                .build();

        // then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // when
            userService.registerUser(requestDto);
        });
        Assertions.assertEquals(UserService.INVALID_PASSWORD, exception.getMessage());
    }

    @Test
    public void findUserById() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .username("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        // when
        FindUserResponseDto findUser = userService.findUserById(1L);

        // then
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(userId, findUser.getUserId());
        Assertions.assertEquals(user.getUsername(), findUser.getUsername());
        Assertions.assertEquals(user.getEmail(), findUser.getEmail());
    }

    @Test
    public void testFindUserByIdNotExists() {
        // given
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // when
            userService.findUserById(userId);
        });
        Assertions.assertEquals(UserService.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    public void testUpdateUser() {
// given
        Long userId = 1L;
        User user = User.builder()
                .username("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User findUser = userRepository.findByEmail(user.getEmail());

        String updatedUsername = "testtesttest";
        String updatedEmail = "update_test@gmail.com";

        findUser.setUsername(updatedUsername);
        findUser.setEmail(updatedEmail);

        User savedUser = userRepository.save(findUser);

        // then
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(userId, findUser.getId());
        Assertions.assertEquals(updatedUsername, savedUser.getUsername());
        Assertions.assertEquals(updatedEmail, savedUser.getEmail());
    }
}