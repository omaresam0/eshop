package com.eshop.eshop.service.Cart;

import com.eshop.eshop.model.Cart;
import com.eshop.eshop.model.User;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart intializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
