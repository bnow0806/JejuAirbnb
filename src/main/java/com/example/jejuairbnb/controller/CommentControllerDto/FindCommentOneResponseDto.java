package com.example.jejuairbnb.controller.CommentControllerDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindCommentOneResponseDto {
    private Float rating;
    private String description;
    private String img;
}
