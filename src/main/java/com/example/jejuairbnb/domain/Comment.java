package com.example.jejuairbnb.domain;

import com.example.jejuairbnb.shared.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Product product;
}
