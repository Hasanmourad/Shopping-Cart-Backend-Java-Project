package com.hasanmo.dreamshops.repository;

import com.hasanmo.dreamshops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
    //Category findById(Long id);
    boolean existsByName(String name);


}
