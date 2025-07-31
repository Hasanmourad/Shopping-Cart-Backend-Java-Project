package com.hasanmo.dreamshops.service.order;

import com.hasanmo.dreamshops.dto.OrderDto;
import com.hasanmo.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
