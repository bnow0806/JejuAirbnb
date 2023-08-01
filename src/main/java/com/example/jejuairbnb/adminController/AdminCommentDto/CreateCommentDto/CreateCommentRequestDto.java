package com.example.jejuairbnb.adminController.AdminCommentDto.CreateCommentDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCommentRequestDto {
    @Schema(name = "rating")
    private Float rating;
    @Schema(name = "description")
    private String description;
    @Schema(name = "img")
    private String img;
    @Schema(name = "userId")
    private Long userId;
}
