package com.hasanmo.dreamshops.repository;

import com.hasanmo.dreamshops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

}
