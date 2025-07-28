package com.hasanmo.dreamshops.service.product;

import com.hasanmo.dreamshops.dto.ImageDto;
import com.hasanmo.dreamshops.dto.ProductDto;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Category;
import com.hasanmo.dreamshops.model.Image;
import com.hasanmo.dreamshops.model.Product;
import com.hasanmo.dreamshops.repository.CategoryRepository;
import com.hasanmo.dreamshops.repository.ImageRepository;
import com.hasanmo.dreamshops.repository.ProductRepository;
import com.hasanmo.dreamshops.request.AddProductRequest;
import com.hasanmo.dreamshops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Implementation here

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }
    private Product createProduct(AddProductRequest request, Category category) {

        return new Product(
            request.getId(),
            request.getBrand(),
            request.getDescription(),
            request.getName(),
            request.getPrice(),
            request.getInventory(),
            category
        );
    }

    @Override
    public List<Product> getAllProducts() {
        // Implementation here
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        // Implementation here
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption("Product not found"));
    }

    @Override
    public Product updateProduct(Long id, ProductUpdateRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundExeption("Product not found"));

    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());

        Category category = categoryRepository.findByName(request.getCategory().getName());

        existingProduct.setCategory(category);

        return existingProduct;
    }



    @Override
    public void deleteProductById(Long id) {
        // Implementation here
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundExeption("Product not found");
        });
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // Implementation here
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        // Implementation here
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        // Implementation here
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        // Implementation here
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        // Implementation here
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        // Implementation here
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public ProductDto convertToDto(Product product)
    {
        ProductDto productDto = modelMapper.map(product , ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;

    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products)
    {
        return products.stream().map(this::convertToDto).toList();
    }


}
