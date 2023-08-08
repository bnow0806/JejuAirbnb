package com.example.jejuairbnb.services;

import com.example.jejuairbnb.adminController.AdminCommentDto.CreateCommentDto.CreateCommentRequestDto;
import com.example.jejuairbnb.adminController.AdminCommentDto.UpdateCommentDto.UpdateCommentRequestDto;
import com.example.jejuairbnb.adminServices.AdminCommentService;
import com.example.jejuairbnb.controller.ProductControllerDto.FindProductOneResponseDto;
import com.example.jejuairbnb.domain.Comment;
import com.example.jejuairbnb.domain.Product;
import com.example.jejuairbnb.domain.User;
import com.example.jejuairbnb.repository.ICommentRepository;
import com.example.jejuairbnb.repository.IProductRepository;
import com.example.jejuairbnb.shared.Enum.ProviderEnum;
import com.example.jejuairbnb.shared.exception.HttpException;
import com.example.jejuairbnb.shared.response.CoreSuccessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @MockBean
    private IProductRepository productRepository;

    @Autowired
    private ProductService productService;


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

        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@kakao.com");
        mockUser.setKakaoAuthId("123151223");
        mockUser.setProvider(ProviderEnum.FALSE);
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
        mockComment.setUser(mockUser);

        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockComment));

        // when
        CoreSuccessResponse response = adminCommentService.updateComment(id, mockUser, requestDto);

        // then
        Assertions.assertEquals(response.getMessage(), "댓글이 수정되었습니다.");
        Assertions.assertEquals(response.getStatusCode(), 200);
        Assertions.assertTrue(response.isOk());
    }

    @Test
    public void testUpdateCommentUpdatedAt() {
        //given
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@kakao.com");
        mockUser.setKakaoAuthId("123151223");
        mockUser.setProvider(ProviderEnum.FALSE);

        Long commentId = 1L;
        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setRating(3.5f);
        mockComment.setDescription("Test Descript");
        mockComment.setImg("Test image");
        mockComment.setUser(mockUser);
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
        mockComment.setUser(user);

        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockComment));

        // when
        CoreSuccessResponse response = adminCommentService.deleteComment(id, user);

        // then
        Assertions.assertEquals(response.getMessage(), "댓글이 삭제되었습니다.");
        Assertions.assertEquals(response.getStatusCode(), 200);
        Assertions.assertTrue(response.isOk());
    }

    @Test
    public void testEnrollCommentToProduct() {
        //@OneToMany 관계에서 insert 하기
        //given
        User user = new User();
        user.setProvider(ProviderEnum.FALSE);
        user.setId(1L);

        Product mockProduct1 = new Product();
        mockProduct1.setId(1L);
        mockProduct1.setName("Test product 1");
        mockProduct1.setImg("Test image 1");
        mockProduct1.setPrice(100);

        Comment mockComment1 = new Comment();
        mockComment1.setId(1L);
        mockComment1.setRating(3.51f);
        mockComment1.setDescription("Test Descript1");
        mockComment1.setImg("Test image1");
        mockComment1.setUser(user);

        Comment mockComment2 = new Comment();
        mockComment2.setId(2L);
        mockComment2.setRating(3.52f);
        mockComment2.setDescription("Test Descript2");
        mockComment2.setImg("Test image2");
        mockComment2.setUser(user);

        mockProduct1.getComment().add(mockComment1);
        mockProduct1.getComment().add(mockComment2);
        mockComment1.setProduct(mockProduct1);
        mockComment2.setProduct(mockProduct1);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockProduct1));

        // When
        Product findProduct = productRepository.findById(1L).get();

        // then
        Assertions.assertEquals(findProduct.getComment().size(),2);
        Assertions.assertEquals(findProduct.getComment().get(0),mockComment1);
        Assertions.assertEquals(findProduct.getComment().get(1),mockComment2);
        Assertions.assertEquals(findProduct.getComment().get(0).getProduct(),mockProduct1);
    }

    @Test
    public void testDeleteCommentFromProduct() {
        //@OneToMany 관계에서 delete 하기
        //given
        User user = new User();
        user.setProvider(ProviderEnum.FALSE);
        user.setId(1L);

        Product mockProduct1 = new Product();
        mockProduct1.setId(1L);
        mockProduct1.setName("Test product 1");
        mockProduct1.setImg("Test image 1");
        mockProduct1.setPrice(100);

        Comment mockComment1 = new Comment();
        mockComment1.setId(1L);
        mockComment1.setRating(3.51f);
        mockComment1.setDescription("Test Descript1");
        mockComment1.setImg("Test image1");
        mockComment1.setUser(user);

        Comment mockComment2 = new Comment();
        mockComment2.setId(2L);
        mockComment2.setRating(3.52f);
        mockComment2.setDescription("Test Descript2");
        mockComment2.setImg("Test image2");
        mockComment2.setUser(user);

        mockProduct1.getComment().add(mockComment1);
        mockProduct1.getComment().add(mockComment2);
        mockComment1.setProduct(mockProduct1);
        mockComment2.setProduct(mockProduct1);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockProduct1));

        // when //id, user
        Product findProduct = productRepository.findById(1L).get();
        List<Comment> comments = findProduct.getComment();

        // TODO : fincProduct 내의 comments 중 어느 것을 지울 지에대한 기준 필요
//        for (Comment commentTemp : comments){
//            if(commentTemp.getId()==1L) {
//                comments.remove(commentTemp);
//            }
//        }

        comments.remove(mockComment1);
        System.out.println("findProduct.getComment().size() :"+findProduct.getComment().size());
        if(comments.isEmpty()){
            findProduct.setComment(null);
        }

        // then
        Assertions.assertEquals(findProduct.getComment().size(),1);
        Assertions.assertEquals(findProduct.getComment().get(0),mockComment2);
        Assertions.assertNotNull(findProduct.getComment());
    }

}
