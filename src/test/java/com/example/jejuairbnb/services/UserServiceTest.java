package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.FindUserDto.FindUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.UpdateUserDto.UpdateUserRequestDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.webjars.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import static com.example.jejuairbnb.services.UserService.DUPLICATE_EMAIL;
import static org.mockito.ArgumentMatchers.any;

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
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        CreateUserResponseDto responseDto = userService.registerUser(requestDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(requestDto.getUsername(), responseDto.getUsername());
        Assertions.assertEquals(requestDto.getEmail(), responseDto.getEmail());
    }

    @Test
    public void testExistEmail() throws NoSuchAlgorithmException {
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

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(requestDto);
        });
        Assertions.assertEquals(DUPLICATE_EMAIL, exception.getMessage());
    }

    @Test
    public void invalidPasswordAndRePassword() {
        CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
                .username("test")
                .password("test")
                .rePassword("t")
                .email("test@gmail.com")
                .build();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(requestDto);
        });
        Assertions.assertEquals(UserService.INVALID_PASSWORD, exception.getMessage());
    }

    @Test
    public void findUserById() {
        Long userId = 1L;
        User user = User.builder()
                .username("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();
        user.setId(userId); //Added for test //AutoEncrementation

        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        FindUserResponseDto findUser = userService.findUserById(1L);
        System.out.println(findUser);
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(userId, findUser.getUserId());
        Assertions.assertEquals(user.getUsername(), findUser.getUsername());
        Assertions.assertEquals(user.getEmail(), findUser.getEmail());
    }

    @Test
    public void testFindUserByIdNotExists() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.findUserById(userId);
        });
        Assertions.assertEquals(UserService.NOT_FOUND_USER, exception.getMessage());
    }

    @Test
    public void shouldUpdateUser() {
        // given
        UpdateUserRequestDto requestDto = UpdateUserRequestDto.builder()
                .email("test@gmail.com")
                .username("test_username")
                .password("test_password")
                .rePassword("test_password")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setUsername("test_username");

        Mockito.when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(user);

        // when
        FindUserResponseDto responseDto = userService.updateUser(requestDto);

        // then
        Assertions.assertEquals(user.getId(), responseDto.getUserId());
        Assertions.assertEquals(user.getEmail(), responseDto.getEmail());
        Assertions.assertEquals(user.getUsername(), responseDto.getUsername());
    }

    @Test
    public void shouldThrowUserNotFoundException() {
        // given
        UpdateUserRequestDto requestDto = UpdateUserRequestDto.builder()
                .email("test@gmail.com")
                .username("test_username")
                .password("test_password")
                .rePassword("test_password")
                .build();

        Mockito.when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(null);

        // when
        Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(requestDto));
    }
}