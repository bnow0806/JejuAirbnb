package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProviderControllerDto.CreateProviderDto.CreateProviderRequestDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto.LoginProviderRequestDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto.LoginProviderResponseDto;
import com.example.jejuairbnb.domain.Provider;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IProviderRepository;
import com.example.jejuairbnb.repository.IUserRepository;
import com.example.jejuairbnb.shared.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.jejuairbnb.services.UserService.DUPLICATE_EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class ProviderLoginTest {

    @MockBean // 스프링 부트 테스트에서 사용하는 목(mock) 객체를 생성하는 어노테이션입니다
    private IProviderRepository providerRepository;
    private SecurityService securityService;
    private ProviderService providerService;

    @MockBean
    private IUserRepository userRepository;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        securityService = new SecurityService(providerRepository);
        providerService = new ProviderService(providerRepository, securityService);
    }

    @Test
    public void testEmailForm() throws NoSuchAlgorithmException{

        // given
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .providername("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        // when
        boolean chekEmailFormResult = chekEmailForm(requestDto);

        //정규 표현식 확인
        // then
        Assertions.assertTrue(chekEmailFormResult);
    }

    //TODO : chekEmailForm -> 어디에 구현할까?
    private boolean chekEmailForm(CreateProviderRequestDto requestDto){

        String email = requestDto.getEmail();

        String regx = "^(.+)@(.+)$";

        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Test
    public void testResignedProvider() throws NoSuchAlgorithmException{

        // Test Scenario
        // 1. 회원 탈퇴한 객체 생성
        // 2. 회원 탈퇴 이메일로 신규 가입 요청
        // 3. unregisteredID 인지 확인 -> true 라면 exception 발생 시킬 것

        // given
        // 1. 회원 탈퇴한 객체 생성
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
                //.email("test2@gmail.com")
                .build();

        // TODO : 회원 탈퇴 과정에서 진행 -> Service로 만들기
        existingProvider.setUnregisteredID(1L);

        Mockito.when(providerRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingProvider));

        // 2. 회원 탈퇴 이메일로 신규 가입 요청
        // register 과정 에서 findProvider 찾기   //sqve되면 안되므로 우선 찾기만 -> register에 추후 등록
        // when
        Provider findProvider = providerRepository.findByEmail(requestDto.getEmail()).orElse(null);

        // 3. unregisteredID 인지 확인
        // then
        Long unregisteredID = findProvider.getUnregisteredID();
        Assertions.assertEquals(1L, unregisteredID);
    }

    @Test
    public void testResignProvider() throws NoSuchAlgorithmException{
        //id, pw 입력하여 객체를 찾음
        //unregisteredID 를 1로 만듬
        // given
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .providername("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        Long providerId = 1L;
        Provider provider = Provider.builder()
                .providername("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();
        provider.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(providerRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(provider));
        Mockito.when(providerRepository.save(any(Provider.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        Provider findProvider = providerRepository.findByEmail(requestDto.getEmail()).orElse(null);

        Long updatedUnregisteredID = 1L;
        findProvider.setUnregisteredID(updatedUnregisteredID);

        Provider savedProvider = providerRepository.save(findProvider);

        // then
        Assertions.assertNotNull(findProvider);
        Assertions.assertEquals(providerId, findProvider.getId());
        Assertions.assertEquals(updatedUnregisteredID, savedProvider.getUnregisteredID());
    }

    @Test
    public void testUserOrProvider() throws NoSuchAlgorithmException{
        //findByEmail을 이용
        //1. Provider DB Table에 값이 있는지 확인
        //2. User DB Table에 값이 없는지 확인

        //given
        CreateProviderRequestDto requestDto = CreateProviderRequestDto.builder()
                .providername("test")
                .password("test")
                .rePassword("test")
                .email("test@gmail.com")
                .build();

        Long providerId = 1L;
        Provider provider = Provider.builder()
                .providername("testtest")
                .password("test")
                .email("test@gmail.com")
                .build();
        provider.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(providerRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(provider));
        Mockito.when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(null);

        // when
        Provider findProvider = providerRepository.findByEmail(requestDto.getEmail()).orElse(null);
        User findUser = userRepository.findByEmail(requestDto.getEmail());

        //then
        Assertions.assertNotNull(findProvider);
        Assertions.assertNull(findUser);
    }

    @Test
    public void testLogin() throws NoSuchAlgorithmException{
        //로그인 서비스를 실행해본다.

        // given
        LoginProviderRequestDto requestDto = LoginProviderRequestDto.builder()
                .password("test")
                .email("test@gmail.com")
                .build();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestDto.getPassword().getBytes(StandardCharsets.UTF_8));

        String hashingPassword = Base64.getEncoder().encodeToString(hash);

        //password 처리
        byte[] decoded = Base64.getDecoder().decode(hashingPassword);
        String Password = new String(decoded);

        Provider existingProvider = Provider.builder()
                .providername("test")
                .password(hashingPassword)
                //.password(Password)
                .email(requestDto.getEmail())
                .build();

        //HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        Mockito.when(providerRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(existingProvider));

        // when
        LoginProviderResponseDto loginProviderResponseDto = providerService.loginProvider(requestDto, response);

        // then
        Assertions.assertNotNull(loginProviderResponseDto);
        Assertions.assertEquals(requestDto.getEmail(), loginProviderResponseDto.getEmail());
        Assertions.assertNotNull(loginProviderResponseDto.getToken());
    }
}
