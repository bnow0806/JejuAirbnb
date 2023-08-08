package com.example.jejuairbnb.controller.CommentControllerDto;

import com.example.jejuairbnb.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FindCommentResponseDto {
    private List<Comment> comments;
    private int size;
    private int totalPages;
}
