package com.eshop.eshop.controller;

import com.eshop.eshop.exception.ResourceNotFound;
import com.eshop.eshop.model.Cart;
import com.eshop.eshop.model.User;
import com.eshop.eshop.response.ApiResponse;
import com.eshop.eshop.service.Cart.CartItemService;
import com.eshop.eshop.service.Cart.CartService;
import com.eshop.eshop.service.User.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItem(@RequestParam Long productId, @RequestParam Integer quantity){
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart=  cartService.intializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Item added successfully",null));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch(JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/cart/{cartId}/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItem(@PathVariable  Long cartId,
                                                  @PathVariable Long productId){
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Item removed successfully",null));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId,itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item updated successfully",null));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
