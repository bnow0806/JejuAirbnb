package com.example.jejuairbnb.controller;

import com.example.jejuairbnb.adminController.AdminCommentDto.CreateCommentDto.CreateCommentRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserRequestDto;
import com.example.jejuairbnb.controller.UserControllerDto.CreateUserDto.CreateUserResponseDto;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.services.CommentService;
import com.example.jejuairbnb.services.UserService;
import com.example.jejuairbnb.shared.response.CoreSuccessResponseWithData;
import com.example.jejuairbnb.shared.services.SecurityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "comments", description = "댓글 API")
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final SecurityService securityService;

    @PostMapping("/kakao_login")
    public CoreSuccessResponseWithData createComment(
            @RequestBody CreateCommentRequestDto requestDto,
            @CookieValue("access-token") String accessToken
    ) {
        User findUser = securityService.getSubject(accessToken);
        return commentService.createComment(
                findUser,
                requestDto
        );
    }
}
