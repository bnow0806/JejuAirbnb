package com.example.jejuairbnb.domain;

import com.example.jejuairbnb.shared.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
//@SequenceGenerator(
//        name="PRODUCT_SEQ_GEN", //시퀀스 제너레이터 이름
//        sequenceName="PRODUCT_SEQ", //시퀀스 이름
//        initialValue=1, //시작값
//        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
//)
public class Product extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(
//            strategy=GenerationType.SEQUENCE, //사용할 전략을 시퀀스로  선택
//            generator="PRODUCT_SEQ_GEN" //식별자 생성기를 설정해놓은  PRODUCT_SEQ_GEN으로 설정
//    )
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "img")
    private String img;

    @Column(name = "user_id")
    private Long userId;
}
