package com.example.jejuairbnb.adminServices;

import com.example.jejuairbnb.adminController.AdminCommentDto.CreateCommentDto.CreateCommentRequestDto;
import com.example.jejuairbnb.adminController.AdminCommentDto.UpdateCommentDto.UpdateCommentRequestDto;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.shared.exception.HttpException;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class AdminCommentService {

    private final ICommentRepository commentRepository;

    @Transactional
    public CoreSuccessResponse createComment(
            User user,
            CreateCommentRequestDto requestDto
    ) {
        if (requestDto.getDescription() == null) {
            throw new HttpException(
                    false,
                    "description 이 비어있습니다.",
                    HttpStatus.NOT_FOUND
            );
        }
        if (requestDto.getDescription().length() > 1000) {
            throw new HttpException(
                    false,
                    "description 길이가 너무 깁니다.",
                    HttpStatus.BAD_REQUEST
            );
        }

        Comment newComment = Comment
                .builder()
                .rating(requestDto.getRating())
                .description(requestDto.getDescription())
                .img(requestDto.getImg())
                .user(user)
                .build();

        commentRepository.save(newComment);
        return new CoreSuccessResponse(
                true,
                "댓글이 등록되었습니다.",
                201
        );
    }

    @Transactional
    public CoreSuccessResponse updateComment(
            Long id,
            User user,
            UpdateCommentRequestDto updateCommentRequestDto
    ) {
        Comment findComment = commentRepository.findById(id).orElseThrow(
                () -> new HttpException(
                        false,
                        "해당 댓글이 없습니다.",
                        HttpStatus.NOT_FOUND
                )
        );

        if (user.getId() != findComment.getUser().getId()){
            throw new HttpException(
                    false,
                    "댓글 작성자가 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        findComment.setRating(updateCommentRequestDto.getRating());
        findComment.setDescription(updateCommentRequestDto.getDescription());
        findComment.setImg(updateCommentRequestDto.getImg());
        findComment.setUser(user);
        commentRepository.save(findComment);

        return new CoreSuccessResponse(
                true,
                "댓글이 수정되었습니다.",
                200
        );
    }

    @Transactional
    public CoreSuccessResponse deleteComment(
            Long id,
            User user
    ) {
        Comment findComment = commentRepository.findById(id)
                .orElseThrow(
                        () -> new HttpException(
                                false,
                                "해당 댓글이 없습니다.",
                                HttpStatus.NOT_FOUND
                        )
                );

        if (findComment.getUser().getId() != user.getId()) {
            throw new HttpException(
                    false,
                    "해당 댓글의 작성자가 아닙니다.",
                    HttpStatus.NOT_FOUND
            );
        }

        commentRepository.delete(findComment);
        return new CoreSuccessResponse(
                true,
                "댓글이 삭제되었습니다.",
                200
        );
    }
}
