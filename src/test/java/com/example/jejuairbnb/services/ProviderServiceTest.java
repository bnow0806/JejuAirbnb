package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto.CreateProviderRequestDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto.CreateProviderResponseDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.FindProviderDto.FindProviderResponseDto;
import com.example.jejuairbnb.domain.Provider;
import com.example.jejuairbnb.repository.IProviderRepository;
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
    private IProviderRepository providerRepository;

    private ProviderService providerService;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        providerService = new ProviderService(providerRepository);
    }

    @Test
    public void testRegisterProvider() throws NoSuchAlgorithmException {
        // given
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .providername("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestDto.getPassword().getBytes(StandardCharsets.UTF_8));

        String hashingPassword = Base64.getEncoder().encodeToString(hash);

        Provider provider = Provider.builder()
                .providername(requestDto.getProvidername())
                .password(hashingPassword)
                .email(requestDto.getEmail())
                .build();

        Mockito.when(providerRepository.findByEmail(requestDto.getEmail())).thenReturn(null);
        Mockito.when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        CreateProviderResponseDto responseDto = providerService.registerProvider(requestDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(requestDto.getProvidername(), responseDto.getProvidername());
        Assertions.assertEquals(requestDto.getEmail(), responseDto.getEmail());
    }

    @Test
    public void testExistEmail() throws NoSuchAlgorithmException {
        // requestDto 으로 일단 기본 셋팅을 한다.
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .providername("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestDto.getPassword().getBytes(StandardCharsets.UTF_8));

        String hashingPassword = Base64.getEncoder().encodeToString(hash);

        Provider existingProvider = Provider.builder()
                .providername(requestDto.getProvidername())
                .password(hashingPassword)
                .email(requestDto.getEmail())
                .build();

        Mockito.when(providerRepository.findByEmail(requestDto.getEmail())).thenReturn(existingProvider);
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
    public void invalidPasswordAndRePassword() {
        // given
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .providername("test")
                .password("test")
                .rePassword("t")
                .email("test@gmail.com")
                .build();

        // then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // when
            providerService.registerProvider(requestDto);
        });
        Assertions.assertEquals(ProviderService.INVALID_PASSWORD, exception.getMessage());
    }

    @Test
    public void findProviderById() {
        // given
        Long providerId = 1L;
        Provider provider = Provider.builder()
                .providername("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();
        provider.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(providerRepository.findById(1L)).thenReturn(java.util.Optional.of(provider));

        // when
        FindProviderResponseDto findProvider = providerService.findProviderById(1L);

        // then
        Assertions.assertNotNull(findProvider);
        Assertions.assertEquals(providerId, findProvider.getProviderId());
        Assertions.assertEquals(provider.getProvidername(), findProvider.getProvidername());
        Assertions.assertEquals(provider.getEmail(), provider.getEmail());
    }

    @Test
    public void testFindProivderByIdNotExists() {
        // given
        Long providerId = 1L;

        Mockito.when(providerRepository.findById(providerId)).thenReturn(Optional.empty());

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
        Provider provider = Provider.builder()
                .providername("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();
        provider.setId(providerId); //Added for test //AutoEncrementation

        //Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(providerRepository.findByEmail(provider.getEmail())).thenReturn(provider); //Added for Test
        Mockito.when(providerRepository.save(any(Provider.class))).then(AdditionalAnswers.returnsFirstArg());   //Added for test

        // when
        Provider findProvider = providerRepository.findByEmail(provider.getEmail());

        String updatedProvidername = "testtesttest";
        String updatedEmail = "update_test@gmail.com";

        findProvider.setProvidername(updatedProvidername);
        findProvider.setEmail(updatedEmail);

        Provider savedProvider = providerRepository.save(findProvider);

        // then
        Assertions.assertNotNull(findProvider);
        Assertions.assertEquals(providerId, findProvider.getId());
        Assertions.assertEquals(updatedProvidername, savedProvider.getProvidername());
        Assertions.assertEquals(updatedEmail, savedProvider.getEmail());
    }
}