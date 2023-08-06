package com.example.jejuairbnb.repository;

import com.example.jejuairbnb.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByUserId(Long userId, Pageable pageable);
    List<Comment> findByDescriptionContaining(String description);
    Page<Comment> findByDescriptionContaining(String description, Pageable pageable);
}
