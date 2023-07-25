package com.example.jejuairbnb.repository;

import com.example.jejuairbnb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoAuthId(String kakaoAuthId);
}
