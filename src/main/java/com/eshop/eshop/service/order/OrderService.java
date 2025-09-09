package com.eshop.eshop.service.order;

import com.eshop.eshop.dto.OrderDto;
import com.eshop.eshop.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
