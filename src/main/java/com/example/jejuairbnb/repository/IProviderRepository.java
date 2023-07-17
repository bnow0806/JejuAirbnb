package com.example.jejuairbnb.repository;

import com.example.jejuairbnb.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByEmail(String email);
}
