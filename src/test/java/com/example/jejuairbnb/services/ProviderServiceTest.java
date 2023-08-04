package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto.CreateProviderRequestDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto.CreateProviderResponseDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.FindProviderDto.FindProviderResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import com.example.jejuairbnb.shared.services.SecurityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import static com.example.jejuairbnb.services.UserService.DUPLICATE_EMAIL;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ProviderServiceTest {

    @MockBean // 스프링 부트 테스트에서 사용하는 목(mock) 객체를 생성하는 어노테이션입니다
    private IUserRepository userRepository;
    private SecurityService securityService;
    private ProviderService providerService;
    private SocialLoginService socialLoginService;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        providerService = new ProviderService(userRepository, socialLoginService, securityService);
    }

    @Test
    public void testCreateProvider()
            throws NoSuchAlgorithmException
    {
        // given
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .kakaoToken("asdlfkasdfkljhasdkfjhasdkjfh")
                .build();

        // when


        User provider = User.builder()
                .kakaoAuthId(requestDto.getKakaoToken())
                .build();

        Mockito.when(userRepository.findByEmail(requestDto.getKakaoToken())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(provider);

        CreateProviderResponseDto responseDto = providerService.registerProvider(requestDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(requestDto.getKakaoToken(), responseDto.getProvidername());
    }

    @Test
    public void testExistEmail() {
        // requestDto 으로 일단 기본 셋팅을 한다.
        User existingProvider = User.builder()
                .username("ash")
                .email("ash@gmail.com")
                .kakaoAuthId("asdlfkasdfkljhasdkfjhasdkjfh")
                .build();

        String kakaoToken = existingProvider.getKakaoAuthId();

        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .kakaoToken(kakaoToken)
                .build();

        Mockito.when(userRepository.findByEmail(existingProvider.getEmail())).thenReturn(Optional.of(existingProvider));
        // 위에서 mock 객체를 생성했기 때문에, userRepository.findByEmail() 메소드를 호출하면
        // existingUser 객체를 리턴하도록 설정 한다.

        // then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // when
            providerService.registerProvider(requestDto);
        });
        Assertions.assertEquals(DUPLICATE_EMAIL, exception.getMessage());
    }

    @Test
    public void testFindProviderById() {
        // given
        Long providerId = 1L;
        User provider = User.builder()
                .username("testtest")
                .email("test@gmail.com")
                .build();
        provider.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(provider));

        // when
        FindProviderResponseDto findProvider = providerService.findProviderById(1L);

        // then
        Assertions.assertNotNull(findProvider);
        Assertions.assertEquals(providerId, findProvider.getProviderId());
        Assertions.assertEquals(provider.getUsername(), findProvider.getUsername());
        Assertions.assertEquals(provider.getEmail(), provider.getEmail());
    }

    @Test
    public void testFindProivderByIdNotExists() {
        // given
        Long providerId = 1L;

        Mockito.when(userRepository.findById(providerId)).thenReturn(Optional.empty());

        // then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // when
            providerService.findProviderById(providerId);
        });
        Assertions.assertEquals(ProviderService.NOT_FOUND_PROVIDER, exception.getMessage());
    }

    @Test
    public void testUpdateProvider() {
        // given
        Long providerId = 1L;
        User users = User.builder()
                .username("testtest")
                .email("test@gmail.com")
                .build();
        users.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(userRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(users));
        Mockito.when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());   //Added for test

        // when
        User findUser = userRepository.findByEmail(users.getEmail()).orElse(null);

        String updatedProvidername = "testtesttest";
        String updatedEmail = "update_test@gmail.com";

        findUser.setUsername(updatedProvidername);
        findUser.setEmail(updatedEmail);

        User savedProvider = userRepository.save(findUser);

        // then
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(providerId, findUser.getId());
        Assertions.assertEquals(updatedProvidername, savedProvider.getUsername());
        Assertions.assertEquals(updatedEmail, savedProvider.getEmail());
    }
}