package com.hasanmo.dreamshops.service.cart;

import com.hasanmo.dreamshops.model.Cart;
import com.hasanmo.dreamshops.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
