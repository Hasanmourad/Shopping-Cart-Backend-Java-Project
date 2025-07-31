package com.hasanmo.dreamshops.service.product;

import com.hasanmo.dreamshops.dto.ProductDto;
import com.hasanmo.dreamshops.model.Product;
import com.hasanmo.dreamshops.request.AddProductRequest;
import com.hasanmo.dreamshops.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct (AddProductRequest request);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Long id, ProductUpdateRequest request);

    void deleteProductById(Long id);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    ProductDto convertToDto(Product product);

    List<ProductDto> getConvertedProducts(List<Product> products);
}