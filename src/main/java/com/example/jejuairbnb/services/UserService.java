package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.FindUserDto.FindUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.UpdateUserDto.UpdateUserRequestDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.services.SecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Map;
import java.util.Optional;

import static com.example.jejuairbnb.services.SocialLoginService.DOES_NOT_FOUND_KAKAO_TOKEN;

@Service
@AllArgsConstructor
public class UserService {

    static final String DUPLICATE_EMAIL = "중복된 이메일이 존재합니다.";
    static final String NOT_FOUND_USER = "존재하지 않는 유저입니다.";

    private final IUserRepository userRepository;
    private final SocialLoginService socialLoginService;
    private final SecurityService securityService;


    @Transactional
    public CreateUserResponseDto registerUser(
            CreateUserRequestDto requestDto,
            HttpServletResponse response
    ) {
        System.out.println(requestDto);
        String kakaoToken = requestDto.getKakaoToken();
        Map<String, Object> responseKakaoData = socialLoginService.kakaoCallback(kakaoToken);
        if (responseKakaoData == null) {
            throw new NotFoundException(DOES_NOT_FOUND_KAKAO_TOKEN);
        }

        String kakaoAuthId = responseKakaoData.get("id").toString();
        User findUserByKakaoAuthId = userRepository.findByKakaoAuthId(kakaoAuthId)
                .orElse(null);
        Optional<Map<String, Object>> kakaoAccountOptional = Optional.ofNullable((Map<String, Object>) responseKakaoData.get("kakao_account"));
        String email = kakaoAccountOptional.map(kakaoAccount -> kakaoAccount.get("email").toString()).orElse(null);

        Optional<Map<String, Object>> propertiesOptional = Optional.ofNullable((Map<String, Object>) responseKakaoData.get("properties"));
        String nickname = propertiesOptional.map(properties -> properties.get("nickname").toString()).orElse(null);

        if (findUserByKakaoAuthId == null) {
//           회원가입을 시킨다.
            User newUser = User
                    .builder()
                    .username(nickname)
                    .email(email)
                    .kakaoAuthId(kakaoAuthId)
                    .provider(ProviderEnum.FALSE)
                    .build();

            User savedUser = userRepository.save(newUser);

            return new CreateUserResponseDto(
                    savedUser.getUsername(),
                    savedUser.getEmail()
            );
        } else {
//            로그인을 시킨다.
            String getToken = securityService.createToken(email);
            Cookie cookie = new Cookie("access-token", String.valueOf(getToken));
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return CreateUserResponseDto
                    .builder()
                    .email(email)
                    .username(nickname)
                    .build();
        }
    }

    @Transactional
    public FindUserResponseDto findUserById(
           Long userId
    ) {
      return userRepository.findById(userId)
              .map(user -> FindUserResponseDto.builder()
                      .userId(user.getId())
                      .email(user.getEmail())
                      .username(user.getUsername())
                      .build())
              .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER));
    }


    @Transactional
    public FindUserResponseDto updateUser(
            UpdateUserRequestDto requestDto
    ) {
        return userRepository.findByEmail(requestDto.getEmail())
                .map(user -> {
                    user.setUsername(requestDto.getUsername());
                    user.setEmail(requestDto.getEmail());
                    userRepository.save(user);
                    return FindUserResponseDto.builder()
                            .userId(user.getId())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .build();
                })
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }
}
