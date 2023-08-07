package com.example.jejuairbnb.adminController.AdminCommentDto.UpdateCommentDto;

import lombok.Data;

@Data
public class UpdateCommentRequestDto {
    private Long id;
    private Float rating;
    private String description;
    private String img;
}
