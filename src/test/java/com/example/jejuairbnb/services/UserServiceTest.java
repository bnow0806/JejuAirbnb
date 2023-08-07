package com.example.jejuairbnb.services;

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

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean // 스프링 부트 테스트에서 사용하는 목(mock) 객체를 생성하는 어노테이션입니다
    private IUserRepository userRepository;

    private UserService userService;
    private SocialLoginService socialLoginService;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        userService = new UserService(
                userRepository,
                socialLoginService,
                null);
    }

    @Test
    public void findUserById() {
        Long userId = 1L;
        User user = User.builder()
                .username("testtest")
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
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setUsername("test_username");
        user.setKakaoAuthId("test_kakao_auth_id");

        Mockito.when(userRepository.findByEmail(requestDto.getEmail()));

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
                .build();

        Mockito.when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(null);

        // when
        Assertions.assertThrows(NullPointerException.class, () -> userService.updateUser(requestDto));
    }
}