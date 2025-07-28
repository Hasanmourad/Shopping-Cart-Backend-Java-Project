package com.hasanmo.dreamshops.request;

import com.hasanmo.dreamshops.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {

    private Long id;
    private String brand;
    private String description;
    private String name;
    private BigDecimal price;
    private int inventory;
    private Category category;

}
