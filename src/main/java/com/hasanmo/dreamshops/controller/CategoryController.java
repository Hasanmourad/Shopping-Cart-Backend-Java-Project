package com.hasanmo.dreamshops.controller;

import com.hasanmo.dreamshops.exceptions.AlreadyExistsException;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Category;
import com.hasanmo.dreamshops.response.ApiResponse;
import com.hasanmo.dreamshops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("found",categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> AddCategory(@RequestBody Category category) {
        try {
            Category categoryAdded = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Added",categoryAdded));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category foundCategory = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("found",foundCategory));
        } catch (ResourceNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category foundCategory = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("found",foundCategory));
        } catch (ResourceNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("deleted",null));
        } catch (ResourceNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try {
            Category categoryUpdated = categoryService.updateCategory(category,id);
            return ResponseEntity.ok(new ApiResponse("updated",categoryUpdated));
        } catch (ResourceNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }






}
