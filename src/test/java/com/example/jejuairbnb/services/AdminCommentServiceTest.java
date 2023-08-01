package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminServices.AdminCommentService;
import com.example.jejuairbnb.repository.ICommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AdminCommentServiceTest {
    @MockBean
    private ICommentRepository commentRepository;

    @Autowired
    private AdminCommentService adminCommentService;

    @Test
    public void testCreateComment() {
    }

    @Test
    public void testEnrollCommentToProduct() {
    }

    @Test
    public void testCommentDescriptionLength() {
    }

    @Test
    public void testUpdateComment() {
    }

    @Test
    public void testUpdateCommentUpdatedAt() {
    }

    @Test
    public void testDeleteComment() {
    }

    @Test
    public void testDeleteCommentFromProduct() {
    }

    @Test
    public void testFindCommentbyDescription() {
    }

    @Test
    public void testReadCommentbyPagenationAndDescription() {
    }
}
