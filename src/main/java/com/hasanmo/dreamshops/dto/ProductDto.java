package com.hasanmo.dreamshops.dto;

import com.hasanmo.dreamshops.model.Category;
import com.hasanmo.dreamshops.model.Image;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String brand;
    private String description;
    private String name;
    private BigDecimal price;
    private int inventory;
    private List<ImageDto> images;
    private Category category;
}
