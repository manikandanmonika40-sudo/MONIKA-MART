package com.monikamart.monikamart.controller;

import com.monikamart.monikamart.dto.ApiResponse;
import com.monikamart.monikamart.dto.WishlistRequest;
import com.monikamart.monikamart.entity.WishlistItem;
import com.monikamart.monikamart.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistItem>> getWishlistByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToWishlist(@Valid @RequestBody WishlistRequest request) {
        if (request == null || request.getUserId() == null || request.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("userId and productId are required"));
        }

        try {
            WishlistItem item = wishlistService.addToWishlist(request.getUserId(), request.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Item added to wishlist", item));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<ApiResponse> removeFromWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            wishlistService.removeFromWishlist(userId, productId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from wishlist", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<ApiResponse> removeById(@PathVariable Long wishlistId) {
        try {
            wishlistService.removeById(wishlistId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from wishlist", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkWishlist(
            @RequestParam Long userId,
            @RequestParam Long productId
    ) {
        boolean inWishlist = wishlistService.isInWishlist(userId, productId);
        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }
}
