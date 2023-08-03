package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminController.AdminCommentDto.CreateCommentDto.CreateCommentRequestDto;
import com.example.jejuairbnb.controller.CommentControllerDto.FindCommentOneResponseDto;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;


@SpringBootTest
public class CommentServiceTest {

    @MockBean
    private ICommentRepository commentRepository;

    private CommentService commentService;

    @BeforeEach
    public void setup() {
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void testFindOneCommentById() {
        //given
        Long id = 1L;

        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setRating(3.5f);
        mockComment.setDescription("Test Descript");
        mockComment.setImg("Test image");
        mockComment.setUserId(1L);

        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockComment));

        // 실행
        FindCommentOneResponseDto response = commentService.findCommentOneById(id);

        // 검증
        Assertions.assertEquals(response.getRating(), 3.5f);
        Assertions.assertEquals(response.getDescription(), "Test Descript");
        Assertions.assertEquals(response.getImg(),"Test image");
    }


}
