package com.example.jejuairbnb.domain;

import com.example.jejuairbnb.shared.Enum.PositionEnum;
import com.example.jejuairbnb.shared.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "img")
    private String img;

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public Product(
            String name,
            String description,
            String position,
            int price,
            String img,
            Long userId
    ) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.price = price;
        this.img = img;
        this.userId = userId;
    }
}
