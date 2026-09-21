package com.monikamart.monikamart.service;

import com.monikamart.monikamart.dto.OrderRequest;
import com.monikamart.monikamart.entity.Order;
import com.monikamart.monikamart.entity.OrderItem;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.repository.CartItemRepository;
import com.monikamart.monikamart.repository.OrderRepository;
import com.monikamart.monikamart.repository.ProductRepository;
import com.monikamart.monikamart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Order placeOrder(OrderRequest request) {
        Order order = new Order();

        if (request.getUserId() != null) {
            userRepository.findById(request.getUserId()).ifPresent(order::setUser);
        }

        order.setCustomerName(request.getCustomerName().trim());
        order.setCustomerEmail(request.getCustomerEmail().trim());
        order.setCustomerPhone(request.getCustomerPhone().trim());
        order.setShippingAddress(request.getShippingAddress().trim());
        order.setCity(request.getCity().trim());
        order.setState(request.getState().trim());
        order.setPincode(request.getPincode().trim());
        order.setSubtotal(request.getSubtotal());
        order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0);
        order.setTotalAmount(request.getTotalAmount());
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
        order.setPaymentStatus("COD".equalsIgnoreCase(order.getPaymentMethod()) ? "PENDING" : "PAID");
        order.setOrderStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (OrderRequest.OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setProductName(itemReq.getProductName());
                item.setProductImage(itemReq.getProductImage());
                item.setPrice(itemReq.getPrice());
                item.setQuantity(itemReq.getQuantity());
                item.setTotalPrice(itemReq.getTotalPrice() != null ? itemReq.getTotalPrice() : (itemReq.getPrice() * itemReq.getQuantity()));

                if (itemReq.getProductId() != null) {
                    Optional<Product> prodOpt = productRepository.findById(itemReq.getProductId());
                    if (prodOpt.isPresent()) {
                        Product prod = prodOpt.get();
                        item.setProduct(prod);
                        // Reduce stock safely
                        int remainingStock = Math.max(0, prod.getStock() - itemReq.getQuantity());
                        prod.setStock(remainingStock);
                        productRepository.save(prod);
                    }
                }

                order.addItem(item);
            }
        }

        Order savedOrder = orderRepository.save(order);

        // Clear user cart if order was made by registered user
        if (request.getUserId() != null) {
            try {
                cartItemRepository.deleteByUserId(request.getUserId());
            } catch (Exception ignored) {
            }
        }

        return savedOrder;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (newStatus != null && !newStatus.isBlank()) {
            order.setOrderStatus(newStatus.toUpperCase());
        }
        if (paymentStatus != null && !paymentStatus.isBlank()) {
            order.setPaymentStatus(paymentStatus.toUpperCase());
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (userId != null && (order.getUser() == null || !order.getUser().getId().equals(userId))) {
            throw new RuntimeException("Unauthorized: This order does not belong to the user.");
        }

        if ("DELIVERED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new RuntimeException("Cannot cancel an order that has already been delivered.");
        }

        if (!"CANCELLED".equalsIgnoreCase(order.getOrderStatus())) {
            // Restore inventory stock for each product
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    if (item.getProduct() != null) {
                        Product product = item.getProduct();
                        int currentStock = product.getStock() != null ? product.getStock() : 0;
                        product.setStock(currentStock + item.getQuantity());
                        productRepository.save(product);
                    }
                }
            }
            order.setOrderStatus("CANCELLED");
        }

        return orderRepository.save(order);
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public double calculateTotalRevenue() {
        Double total = orderRepository.calculateTotalRevenue();
        return total != null ? total : 0.0;
    }
}
