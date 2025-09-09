package com.eshop.eshop.service.order;

import com.eshop.eshop.dto.OrderDto;
import com.eshop.eshop.enums.OrderStatus;
import com.eshop.eshop.model.Cart;
import com.eshop.eshop.model.Order;
import com.eshop.eshop.model.OrderItem;
import com.eshop.eshop.model.Product;
import com.eshop.eshop.repository.OrderRepository;
import com.eshop.eshop.repository.ProductRepository;
import com.eshop.eshop.service.Cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(calcTotalAmount(orderItemsList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setStockCount(product.getStockCount() - cartItem.getStockCount());
            productRepository.save(product);
            return new OrderItem(order, product, cartItem.getStockCount(), cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calcTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this :: convertToDto).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this :: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}