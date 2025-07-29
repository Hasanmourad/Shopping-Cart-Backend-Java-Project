package com.hasanmo.dreamshops.service.cart;

import com.hasanmo.dreamshops.exceptions.CustomConflictException;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Cart;
import com.hasanmo.dreamshops.repository.CartItemRepository;
import com.hasanmo.dreamshops.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements  ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);


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
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    @Transactional
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        newCart.setTotalAmount(BigDecimal.ZERO);
        newCart.setItems(new HashSet<>());
        Cart savedCart = cartRepository.save(newCart);
        return savedCart.getId();
    }
}
