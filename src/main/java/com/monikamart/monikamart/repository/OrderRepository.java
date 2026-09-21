package com.monikamart.monikamart.repository;

import com.monikamart.monikamart.entity.Order;
import com.monikamart.monikamart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
    List<Order> findAllByOrderByOrderDateDesc();
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o WHERE o.orderStatus != 'CANCELLED'")
    Double calculateTotalRevenue();

    long countByOrderStatus(String orderStatus);
}
