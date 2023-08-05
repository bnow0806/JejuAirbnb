package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminController.AdminCommentDto.CreateCommentDto.CreateCommentRequestDto;
import com.example.jejuairbnb.adminController.AdminCommentDto.UpdateCommentDto.UpdateCommentRequestDto;
import com.example.jejuairbnb.adminServices.AdminCommentService;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.exception.HttpException;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class AdminCommentServiceTest {
    @MockBean
    private ICommentRepository commentRepository;

    @Autowired
    private AdminCommentService adminCommentService;
    @Autowired
    private CommentService commentService;

    @Test
    public void testCreateComment() {
        // 주어진 User와 CreateCommentRequestDto 객체 생성
        User user = new User();
        user.setProvider(ProviderEnum.FALSE);
        user.setId(1L);

        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setRating(3.5f);
        requestDto.setDescription("Test Description");
        requestDto.setImg("Test Image URL");
        requestDto.setUserId(1L);   //not used

        // 실행
        CoreSuccessResponse response = adminCommentService.createComment(user, requestDto);

        // 검증
        Assertions.assertEquals(response.getMessage(), "댓글이 등록되었습니다.");
        Assertions.assertEquals(response.getStatusCode(), 201);
        Assertions.assertTrue(response.isOk());
    }

    @Test
    public void testEnrollCommentToProduct() {
    }

    @Test
    public void testCommentDescriptionLength() {
        // 주어진 User와 CreateCommentRequestDto 객체 생성
        User user = new User();
        user.setProvider(ProviderEnum.FALSE);
        user.setId(1L);

        StringBuilder longDescription = new StringBuilder();
        longDescription.append("Test Description".repeat(100));
        String testDescription = longDescription.toString();

        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setRating(3.5f);
        requestDto.setDescription(testDescription);
        requestDto.setImg("Test Image URL");
        requestDto.setUserId(1L);   //not used

        // 실행   // 검증
        Exception exception = Assertions.assertThrows(HttpException.class, () -> {
            adminCommentService.createComment(user, requestDto);
        });

        Assertions.assertEquals("description 길이가 너무 깁니다.", exception.getMessage());
    }

    @Test
    public void testUpdateComment() {
        // given
        Long id = 1L;   //comment id

        User user = new User();
        user.setProvider(ProviderEnum.FALSE);
        user.setId(1L);
        //user.setId(2L);

        UpdateCommentRequestDto requestDto = new UpdateCommentRequestDto();
        requestDto.setRating(3.5f);
        requestDto.setDescription("Test Description");
        requestDto.setImg("Test Image URL");

        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setRating(3.5f);
        mockComment.setDescription("Test Descript");
        mockComment.setImg("Test image");
        mockComment.setUserId(1L);

        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockComment));

        // when
        CoreSuccessResponse response = adminCommentService.updateComment(id, user, requestDto);

        // then
        Assertions.assertEquals(response.getMessage(), "댓글이 수정되었습니다.");
        Assertions.assertEquals(response.getStatusCode(), 200);
        Assertions.assertTrue(response.isOk());
    }

    @Test
    public void testUpdateCommentUpdatedAt() {
        //given
        Long commentId = 1L;
        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setRating(3.5f);
        mockComment.setDescription("Test Descript");
        mockComment.setImg("Test image");
        mockComment.setUserId(1L);
        mockComment.setUpdatedAt(LocalDateTime.now());

        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockComment));
        Mockito.when(commentRepository.save(any(Comment.class))).then(AdditionalAnswers.returnsFirstArg());

        Comment findComment = commentRepository.findById(commentId).orElse(null);

        Float updatedRating = 4.5f;
        String updatedDescription = "Update Descript";

        findComment.setRating(updatedRating);
        findComment.setDescription(updatedDescription);

        Comment savedComment = commentRepository.save(findComment);

        // then
        Assertions.assertNotNull(findComment);
        Assertions.assertEquals(commentId, findComment.getId());
        Assertions.assertEquals(updatedRating, savedComment.getRating());
        Assertions.assertEquals(updatedDescription, savedComment.getDescription());

        System.out.println("updatedRating :"+savedComment.getRating());
        System.out.println("findComment.getUpdatedAt() : "+findComment.getUpdatedAt());
        System.out.println("savedComment.getUpdatedAt() : "+savedComment.getUpdatedAt());
        //Assertions.assertNotEquals(findComment.getUpdatedAt(), savedComment.getUpdatedAt());
    }

    @Test
    public void testDeleteComment() {
        //given
        Long id = 1L;   //comment id

        User user = new User();
        user.setProvider(ProviderEnum.FALSE);
        user.setId(1L);

        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setRating(3.5f);
        mockComment.setDescription("Test Descript");
        mockComment.setImg("Test image");
        mockComment.setUserId(1L);

        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockComment));

        // when
        CoreSuccessResponse response = adminCommentService.deleteComment(id, user);

        // then
        Assertions.assertEquals(response.getMessage(), "댓글이 삭제되었습니다.");
        Assertions.assertEquals(response.getStatusCode(), 200);
        Assertions.assertTrue(response.isOk());
    }

    @Test
    public void testDeleteCommentFromProduct() {
    }

}
