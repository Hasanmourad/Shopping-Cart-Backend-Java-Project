package com.hasanmo.dreamshops.service.category;


import com.hasanmo.dreamshops.exceptions.AlreadyExistsException;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Category;
import com.hasanmo.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;



    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption("category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(category1 -> !categoryRepository.existsByName(category1.getName()) )
                .map(categoryRepository::save)
                .orElseThrow(() ->new AlreadyExistsException(category.getName()+" already exist"));
    }


    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundExeption("category not found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,() -> {throw new ResourceNotFoundExeption("category not found");});

    }
}
