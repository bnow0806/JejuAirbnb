package com.example.jejuairbnb.domain;

import com.example.jejuairbnb.shared.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cpmments")
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

    @Builder
    public Comment(
            Float rating,
            String description,
            String img,
            Long userId
    ) {
        this.rating = rating;
        this.description = description;
        this.img = img;
        this.userId = userId;
    }
}
