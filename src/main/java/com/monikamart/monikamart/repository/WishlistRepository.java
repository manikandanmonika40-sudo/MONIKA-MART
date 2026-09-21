package com.monikamart.monikamart.repository;

import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserOrderByCreatedAtDesc(User user);
    List<WishlistItem> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<WishlistItem> findByUserAndProduct(User user, Product product);
    Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Modifying
    @Transactional
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
