package com.monikamart.monikamart.service;

import com.monikamart.monikamart.dto.CartRequest;
import com.monikamart.monikamart.entity.CartItem;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.repository.CartItemRepository;
import com.monikamart.monikamart.repository.ProductRepository;
import com.monikamart.monikamart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartByUserId(Long userId) {
        return cartItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public CartItem addToCart(CartRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + request.getProductId()));

        int addQty = (request.getQuantity() != null && request.getQuantity() > 0) ? request.getQuantity() : 1;

        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + addQty);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(user, product, addQty);
            return cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with ID: " + cartItemId));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        } else {
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        }
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found with ID: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
