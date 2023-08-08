package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto.LoginProviderRequestDto;
import com.example.jejuairbnb.controller.ProviderControllerDto.LoginProviderDto.LoginResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.services.SecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class ProviderLoginTest {


    @Mock
    private IUserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ProviderService providerService;
    private SocialLoginService socialLoginService;

    @BeforeEach // 각 테스트 메소드 실행 전에 호출되는 메소드를 지정
    public void setup() {
        providerService = new ProviderService(
                userRepository,
                socialLoginService,
                securityService
        );
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
    public void testResignedProvider() {

        // Test Scenario
        // 1. 회원 탈퇴한 객체 생성
        // 2. 회원 탈퇴 이메일로 신규 가입 요청
        // 3. unregisteredID 인지 확인 -> true 라면 exception 발생 시킬 것
        // 3. unregistedID 인지 확인 -> true 라면 exception 발생 시킬 것

        // given
        // 1. 회원 탈퇴한 객체 생성
        String email = "ash@gmail.com";
        String username = "ash";

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
    public void testResignProvider() {
        // given
        Long providerId = 1L;
        User provider = User.builder()
                .username("testtest")
                .email("test@gmail.com")
                .provider(ProviderEnum.FALSE)  // provider 필드를 FALSE로 설정
                .build();

        provider.setId(providerId); // AutoIncrementation

        Mockito.when(userRepository.findByEmail(provider.getEmail())).thenReturn(Optional.of(provider));
        Mockito.when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        User findProvider = userRepository.findByEmail(provider.getEmail()).orElse(null);
        assert findProvider != null;

        findProvider.setProvider(ProviderEnum.TRUE);  // 조건문 바깥으로 이동
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
    public void testLoginProvider() {
        // given
        LoginProviderRequestDto requestDto = new LoginProviderRequestDto(
                "test",
                "test@gmail.com"
        );
        User existingUser = new User();
        existingUser.setEmail(requestDto.getEmail());
        String fakeToken = "fakeToken";

        HttpServletResponse response = mock(HttpServletResponse.class);

        // setting expected behavior
        Mockito.when(userRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(existingUser));

        Mockito.when(securityService.createToken(requestDto.getEmail()))
                .thenReturn(fakeToken);
        // when
        LoginResponseDto loginResponseDto = providerService.loginProvider(requestDto, response);

        // then
        Assertions.assertNotNull(loginResponseDto);
        Assertions.assertEquals(requestDto.getEmail(), loginResponseDto.getEmail());
        Assertions.assertEquals(fakeToken, loginResponseDto.getToken());
        Mockito.verify(response).addCookie(any(Cookie.class));
    }

    @Test
    public void testJwtTokenProvider() throws NoSuchAlgorithmException{
    }
}
