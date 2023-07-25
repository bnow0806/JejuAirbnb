package com.example.jejuairbnb.repository;

import com.example.jejuairbnb.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Query(value = "select product " +
            "from Product product " +
            "where product.userId = :userId")
    List<Product> findByKeywordUserId(@Param("userId") Long userId);

    @Query(value = "select product " +
            "from Product product " +
            "where product.price <= :price")
    List<Product> findByKeywordPrice(@Param("price") Long price);
}
