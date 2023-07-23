package com.example.jejuairbnb.shared.services;

import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.IUserRepository;
import com.example.jejuairbnb.shared.exception.HttpException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SecurityService {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    private final IUserRepository userRepository;

    public String createToken(String subject) {

        if (EXPIRATION_TIME <= 0) {
            throw new RuntimeException("Expiratio time must be greater than zero!");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(signingKey,signatureAlgorithm)
                .compact();
    }

    public User getSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return userRepository.findByEmail(claims.getSubject())
                .orElseThrow(
                        () -> new HttpException(
                                false,
                                "존재하지 않은 사용자입니다.",
                                HttpStatus.NOT_FOUND
                        )
                );
    }

    public String getTokenByCookie(Cookie[] cookies) {
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                token = cookie.getValue();
            }
        }
        return token;
    }
}
