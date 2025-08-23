package com.eshop.eshop.service.Cart;

import com.eshop.eshop.exception.ResourceNotFound;
import com.eshop.eshop.model.Cart;
import com.eshop.eshop.model.CartItem;
import com.eshop.eshop.model.Product;
import com.eshop.eshop.repository.CartItemRepository;
import com.eshop.eshop.repository.CartRepository;
import com.eshop.eshop.service.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {

        // check if product already in cart
        // if yes, then increase quantity
        // if no, then initiate a new cartItem entry
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems().stream().filter(item->item.getProduct().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setStockCount(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else{
            // increase only quantity since it already exists
            cartItem.setStockCount(cartItem.getStockCount() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

;
    }


    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem item = getCartItem(cartId, productId);
        cart.removeItem(item);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream().filter(item -> item.getProduct().getId()
                        .equals(productId))
                .findFirst().ifPresent(item->{
                    item.setStockCount(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return  cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFound("Item not found"));
    }

}
