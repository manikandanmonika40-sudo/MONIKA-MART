package com.monikamart.monikamart.controller;

import com.monikamart.monikamart.dto.ApiResponse;
import com.monikamart.monikamart.dto.CartRequest;
import com.monikamart.monikamart.entity.CartItem;
import com.monikamart.monikamart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> getCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@Valid @RequestBody CartRequest request) {
        try {
            CartItem cartItem = cartService.addToCart(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Item added to cart successfully", cartItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam(required = false) Integer quantity,
            @RequestBody(required = false) Map<String, Integer> body
    ) {
        try {
            int qty = 1;
            if (quantity != null) {
                qty = quantity;
            } else if (body != null && body.containsKey("quantity")) {
                qty = body.get("quantity");
            }
            CartItem updated = cartService.updateCartItemQuantity(cartItemId, qty);
            return ResponseEntity.ok(ApiResponse.success("Cart quantity updated", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable Long cartItemId) {
        try {
            cartService.removeCartItem(cartItemId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
