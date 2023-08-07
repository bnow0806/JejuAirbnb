package com.example.jejuairbnb.controller.ProductControllerDto;

import com.example.jejuairbnb.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FindProductOneResponseDto {
    private String name;
    private String description;
    private String content;
    private String img;
    private Long commentCount;
    private Long commentAvg;
    private int price;
    private List<Comment> comments;
}
