package com.example.jejuairbnb.services;

import com.example.jejuairbnb.repository.ICommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


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

    }


}
