package com.eshop.eshop.controller;

import com.eshop.eshop.dto.OrderDto;
import com.eshop.eshop.exception.ResourceNotFound;
import com.eshop.eshop.model.Order;
import com.eshop.eshop.response.ApiResponse;
import com.eshop.eshop.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    private ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item ordered successfully",orderDto));
        } catch (ResourceNotFound e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    private ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
//            Order order = orderService.getOrder(orderId);
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Item ordered successfully",order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order not found",e.getMessage()));
        }
    }

    @GetMapping("/{userId}/orders")
    private ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> order = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Item ordered successfully",order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Order not found",e.getMessage()));
        }
    }



}
