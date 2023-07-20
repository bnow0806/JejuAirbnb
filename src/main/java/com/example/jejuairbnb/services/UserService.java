package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.FindUserDto.FindUserResponseDto;
import com.example.jejuairbnb.controller.UserControllerDto.UpdateUserDto.UpdateUserRequestDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
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

    @Transactional
    public CreateUserResponseDto registerUser(
            CreateUserRequestDto requestDto
    ) {
        System.out.println("회원가입 요청: " + requestDto);
        String kakaoToken = requestDto.getKakaoToken();
//      kakaoToken 을 이용해서 카카오 API 를 호출해서 유저 정보를 가져온다.
        Map<String, Object> responseKakaoData = socialLoginService.kakaoCallback(kakaoToken);
        if (responseKakaoData == null) {
            throw new NotFoundException(DOES_NOT_FOUND_KAKAO_TOKEN);
        }

        String kakaoAuthId = responseKakaoData.get("id").toString();

        Optional<Map<String, Object>> kakaoAccountOptional = Optional.ofNullable((Map<String, Object>) responseKakaoData.get("kakao_account"));
        String email = kakaoAccountOptional.map(kakaoAccount -> kakaoAccount.get("email").toString()).orElse(null);

        Optional<Map<String, Object>> propertiesOptional = Optional.ofNullable((Map<String, Object>) responseKakaoData.get("properties"));
        String nickname = propertiesOptional.map(properties -> properties.get("nickname").toString()).orElse(null);

        User findUser = (User) userRepository.findByEmail(email)
                .map(user -> {
                    throw new IllegalArgumentException(DUPLICATE_EMAIL);
                })
                .orElseGet(() -> User.builder()
                        .username(nickname)
                        .email(email)
                        .kakaoAuthId(kakaoAuthId)
                        .build());
//       주석 달았다
        User savedUser = userRepository.save(findUser);

        return CreateUserResponseDto.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
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
