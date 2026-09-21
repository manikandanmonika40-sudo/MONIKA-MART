package com.monikamart.monikamart.repository;

import com.monikamart.monikamart.entity.CartItem;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserOrderByCreatedAtDesc(User user);
    List<CartItem> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
