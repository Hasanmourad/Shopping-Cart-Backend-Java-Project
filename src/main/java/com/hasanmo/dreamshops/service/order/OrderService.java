package com.hasanmo.dreamshops.service.order;

import com.hasanmo.dreamshops.dto.OrderDto;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Cart;
import com.hasanmo.dreamshops.model.Order;
import com.hasanmo.dreamshops.model.OrderItem;
import com.hasanmo.dreamshops.model.Product;
import com.hasanmo.dreamshops.repository.OrderRepository;
import com.hasanmo.dreamshops.repository.ProductRepository;
import com.hasanmo.dreamshops.service.cart.CartService;
import enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order saved = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return saved;

    }

    private Order createOrder(Cart cart)
    {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return  order;
    }

    private List<OrderItem> createOrderItems(Order order,Cart cart)
    {
        return cart.getItems().stream().map(cartItem -> {
            Product product= cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(order,product,cartItem.getQuantity(),cartItem.getUnitPrice());

        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream().map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundExeption("order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order)
    {
        return modelMapper.map(order, OrderDto.class);
    }
}
