package com.monikamart.monikamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monikamart.monikamart.dto.OrderRequest;
import com.monikamart.monikamart.entity.Order;
import com.monikamart.monikamart.exception.GlobalExceptionHandler;
import com.monikamart.monikamart.service.OrderService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testPlaceOrder_Success() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("Monika Sharma");
        request.setCustomerEmail("monika@example.com");
        request.setCustomerPhone("+91 9812345678");
        request.setShippingAddress("42 Orchid Ave");
        request.setCity("Mumbai");
        request.setState("Maharashtra");
        request.setPincode("400050");
        request.setSubtotal(100.0);
        request.setTotalAmount(100.0);

        OrderRequest.OrderItemRequest itemReq = new OrderRequest.OrderItemRequest();
        itemReq.setProductId(1L);
        itemReq.setProductName("Test Item");
        itemReq.setPrice(100.0);
        itemReq.setQuantity(1);
        itemReq.setTotalPrice(100.0);
        request.setItems(Arrays.asList(itemReq));

        Order order = new Order();
        order.setId(101L);
        order.setCustomerName("Monika Sharma");
        order.setTotalAmount(100.0);
        order.setOrderStatus("PLACED");

        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(101));
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        Order order = new Order();
        order.setId(101L);
        order.setCustomerName("Monika");
        order.setTotalAmount(100.0);
        order.setOrderStatus("PLACED");

        when(orderService.getOrdersByUserId(1L)).thenReturn(Arrays.asList(order));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101));
    }

    @Test
    void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setId(101L);
        order.setOrderStatus("CANCELLED");

        when(orderService.cancelOrder(eq(101L), eq(1L))).thenReturn(order);

        mockMvc.perform(put("/api/orders/101/cancel?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderStatus").value("CANCELLED"));
    }
}
