package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto.LoginProviderRequestDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto.LoginResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.services.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class ProviderLoginTest {

    @MockBean // 스프링 부트 테스트에서 사용하는 목(mock) 객체를 생성하는 어노테이션입니다
    private IUserRepository userRepository;
    private SecurityService securityService;
    private ProviderService providerService;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        securityService = new SecurityService(userRepository);
    }

    @Test
    public void testEmailForm() throws NoSuchAlgorithmException{

        // given
        String email = "ash@gmail.com";
        String username = "ash";

        // when
        boolean chekEmailFormResult = chekEmailForm(email);

        //정규 표현식 확인
        // then
        Assertions.assertTrue(chekEmailFormResult);
    }

    private boolean chekEmailForm(String email){

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
        // 3. unregistedID 인지 확인 -> true 라면 exception 발생 시킬 것

        // given
        // 1. 회원 탈퇴한 객체 생성
        String email = "";
        String username = "";

        User existingProvider = User.builder()
                .username(username)
                .email(email)
                .build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingProvider));

        // 2. 회원 탈퇴 이메일로 신규 가입 요청
        // register 과정 에서 findProvider 찾기   //sqve되면 안되므로 우선 찾기만 -> register에 추후 등록
        // when
        User findUser = userRepository.findByEmail(email).orElse(null);
        findUser.setProvider(ProviderEnum.TRUE);

        // 3. unregisteredID 인지 확인
        // then
        Assertions.assertEquals(1L, findUser.getId());
    }

    @Test
    public void testResignProvider() throws NoSuchAlgorithmException{
        //id, pw 입력하여 객체를 찾음
        //unregisteredID 를 1로 만듬
        // given

        String email = "ash";
        String username = "ash@gmail.com";

        Long providerId = 1L;
        User provider = User.builder()
                .username("testtest")
                .email("test@gmail.com")
                .build();

        provider.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(provider));
        Mockito.when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        User findProvider = userRepository.findByEmail(email).orElse(null);

        User savedProvider = userRepository.save(findProvider);

        // then
        Assertions.assertNotNull(findProvider);
        Assertions.assertEquals(providerId, findProvider.getId());
        Assertions.assertEquals(ProviderEnum.TRUE, savedProvider.getProvider());
    }

    @Test
    public void testUserOrProvider() throws NoSuchAlgorithmException{
        //findByEmail을 이용
        //1. Provider DB Table에 값이 있는지 확인
        //2. User DB Table에 값이 없는지 확인

        //given
        String email = "ash@gmail.com";

        Long providerId = 1L;
        User provider = User.builder()
                .username("testtest")
                .email("test@gmail.com")
                .build();

        provider.setId(providerId); //Added for test //AutoEncrementation

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(provider));
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        // when
        User findProvider = userRepository.findByEmail(email).orElse(null);
        Optional<User> findUser = userRepository.findByEmail(email);

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

        User existingProvider = User.builder()
                .username("test")
                .email(requestDto.getEmail())
                .build();

        HttpServletResponse response = mock(HttpServletResponse.class);

        Mockito.when(userRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(existingProvider));

        // when
        LoginResponseDto loginProviderResponseDto = providerService.loginProvider(requestDto, response);

        // then
        Assertions.assertNotNull(loginProviderResponseDto);
        Assertions.assertEquals(requestDto.getEmail(), loginProviderResponseDto.getEmail());
        Assertions.assertNotNull(loginProviderResponseDto.getToken());
        // 3. unregistedID 인지 확인
        // then
        User findProvider = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        ProviderEnum providerEnum = Objects.requireNonNull(findProvider).getProvider();
        Assertions.assertEquals(ProviderEnum.TRUE, providerEnum);
    }


    @Test
    public void testJwtTokenProvider() throws NoSuchAlgorithmException{
    }
}
