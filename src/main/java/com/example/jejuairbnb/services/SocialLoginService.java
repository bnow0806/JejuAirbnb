package com.example.jejuairbnb.services;

import com.example.jejuairbnb.controller.UserControllerDto.MyInfoUserDto.MyInfoUserResponseDto;
import com.example.jejuairbnb.shared.exception.HttpException;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@AllArgsConstructor
public class SocialLoginService {

    static final String DOES_NOT_FOUND_KAKAO_TOKEN = "kakao token 값을 확인해주세요";

    @Transactional
    public Map<String, Object> kakaoCallback(String tokenString) {
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        // Set Headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenString);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Send a request
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new HttpException(
                        false,
                        "Kakao login error",
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

            Map<String, Object> responseBody = responseEntity.getBody();

            if(responseBody == null || !responseBody.containsKey("id")) {
                throw new HttpException(
                        false,
                        "Kakao login error",
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

            return responseBody;

        } catch (HttpClientErrorException e) {
            // Handle error
            System.out.println("error: " + e.getMessage());
            throw new HttpException(
                    false,
                    "Kakao login error",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional
    public MyInfoUserResponseDto findKakaUserData(
            Map<String, Object> responseKakaoData
    ) {
        String email = responseKakaoData.get("email").toString();
        Map<String, Object> profile = (Map<String, Object>) responseKakaoData.get("profile");
        String nickname = profile.get("nickname").toString();

        return new MyInfoUserResponseDto(
                email,
                nickname
        );
    }
}
