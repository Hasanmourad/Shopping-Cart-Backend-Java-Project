package com.hasanmo.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String description;
    private String name;
    private BigDecimal price;
    private int inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;


    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    
    public Product(Long id, String brand, String description, String name, BigDecimal price, int inventory, Category category) {
        this.id = id;
        this.brand = brand;
        this.description = description;
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }



}


