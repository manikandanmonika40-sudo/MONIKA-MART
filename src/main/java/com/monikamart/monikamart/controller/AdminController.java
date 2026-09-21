package com.monikamart.monikamart.controller;

import com.monikamart.monikamart.dto.ApiResponse;
import com.monikamart.monikamart.dto.DashboardStats;
import com.monikamart.monikamart.dto.OrderStatusRequest;
import com.monikamart.monikamart.entity.Order;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.service.AdminService;
import com.monikamart.monikamart.service.OrderService;
import com.monikamart.monikamart.service.ProductService;
import com.monikamart.monikamart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, ProductService productService, OrderService orderService, UserService userService) {
        this.adminService = adminService;
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getDashboardStats() {
        DashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics loaded", stats));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts(null, null, null, null, "newest"));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody Product product) {
        try {
            Product created = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Product added successfully to MONIKA MART catalog", created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Product removed successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request
    ) {
        try {
            Order updated = orderService.updateOrderStatus(id, request.getOrderStatus(), request.getPaymentStatus());
            return ResponseEntity.ok(ApiResponse.success("Order status updated to " + updated.getOrderStatus(), updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers().stream()
                .peek(u -> u.setPassword(null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
