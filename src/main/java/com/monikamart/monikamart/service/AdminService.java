package com.monikamart.monikamart.service;

import com.monikamart.monikamart.dto.DashboardStats;
import com.monikamart.monikamart.repository.OrderRepository;
import com.monikamart.monikamart.repository.ProductRepository;
import com.monikamart.monikamart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public DashboardStats getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        Double revenue = orderRepository.calculateTotalRevenue();
        double totalRevenue = revenue != null ? revenue : 0.0;
        long pendingOrders = orderRepository.countByOrderStatus("PLACED") + orderRepository.countByOrderStatus("CONFIRMED");
        long deliveredOrders = orderRepository.countByOrderStatus("DELIVERED");

        return new DashboardStats(totalUsers, totalProducts, totalOrders, totalRevenue, pendingOrders, deliveredOrders);
    }
}
