package com.monikamart.monikamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monikamart.monikamart.dto.DashboardStats;
import com.monikamart.monikamart.dto.OrderStatusRequest;
import com.monikamart.monikamart.entity.Order;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.exception.GlobalExceptionHandler;
import com.monikamart.monikamart.service.AdminService;
import com.monikamart.monikamart.service.OrderService;
import com.monikamart.monikamart.service.ProductService;
import com.monikamart.monikamart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetDashboardStats() throws Exception {
        DashboardStats stats = new DashboardStats(10L, 20L, 5L, 1500.0, 2L, 3L);
        when(adminService.getDashboardStats()).thenReturn(stats);

        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalUsers").value(10))
                .andExpect(jsonPath("$.data.totalRevenue").value(1500.0));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        OrderStatusRequest request = new OrderStatusRequest("SHIPPED");
        request.setPaymentStatus("PAID");

        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus("SHIPPED");
        order.setPaymentStatus("PAID");

        when(orderService.updateOrderStatus(eq(1L), eq("SHIPPED"), eq("PAID"))).thenReturn(order);

        mockMvc.perform(put("/api/admin/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderStatus").value("SHIPPED"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        User u = new User("Admin", "admin@monikamart.com", "+1234567890", "password", "Addr", "City", "State", "12345", "ADMIN");
        u.setId(1L);

        when(userService.getAllUsers()).thenReturn(Arrays.asList(u));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("admin@monikamart.com"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }
}
