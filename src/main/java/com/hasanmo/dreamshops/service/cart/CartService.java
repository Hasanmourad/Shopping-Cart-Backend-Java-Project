package com.hasanmo.dreamshops.service.cart;

import com.hasanmo.dreamshops.exceptions.CustomConflictException;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Cart;
import com.hasanmo.dreamshops.model.User;
import com.hasanmo.dreamshops.repository.CartItemRepository;
import com.hasanmo.dreamshops.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements  ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    public Cart getCart(Long id) {
        try {
            Cart cart = cartRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundExeption("Cart not found"));

            BigDecimal totalAmount = cart.getTotalAmount();
            cart.setTotalAmount(totalAmount);

            return cartRepository.save(cart);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomConflictException("Cart was modified by another process. Please refresh and try again.");
        }
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Cart not found"));
                
        // First clear the items
        cartItemRepository.deleteAllByCartId(id);
        
        // Then delete the cart
        cartRepository.delete(cart);
        
        // Clear user's cart reference if exists
        if (cart.getUser() != null) {
            cart.getUser().setCart(null);
        }
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    @Transactional
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setTotalAmount(BigDecimal.ZERO);
                    newCart.setItems(new HashSet<>());
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });


    }

    @Override
    public Cart getCartByUserId(Long userId)
    {
        return cartRepository.findByUserId(userId);
    }
}
