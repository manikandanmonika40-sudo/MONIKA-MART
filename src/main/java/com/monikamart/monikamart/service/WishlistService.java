package com.monikamart.monikamart.service;

import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.entity.WishlistItem;
import com.monikamart.monikamart.repository.ProductRepository;
import com.monikamart.monikamart.repository.UserRepository;
import com.monikamart.monikamart.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<WishlistItem> getWishlistByUserId(Long userId) {
        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public WishlistItem addToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Optional<WishlistItem> existing = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            return existing.get();
        }

        WishlistItem item = new WishlistItem(user, product);
        return wishlistRepository.save(item);
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void removeById(Long wishlistId) {
        if (!wishlistRepository.existsById(wishlistId)) {
            throw new RuntimeException("Wishlist item not found with ID: " + wishlistId);
        }
        wishlistRepository.deleteById(wishlistId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }
}
