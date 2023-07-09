package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.UserControllerRequestDto;
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

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private IUserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository);
    }

    @Test
    public void testRegisterUser() throws NoSuchAlgorithmException {
        // given
        UserControllerRequestDto requestDto = UserControllerRequestDto.builder()
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
}