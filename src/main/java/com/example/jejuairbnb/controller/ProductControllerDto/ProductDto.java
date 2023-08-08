package com.example.jejuairbnb.controller.ProductControllerDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String position;
    private String description;
    private int price;
    private String img;
    private List<Map<String,String>> reservationDate;
}
