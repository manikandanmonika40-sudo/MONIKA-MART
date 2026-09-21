package com.monikamart.monikamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monikamart.monikamart.dto.CartRequest;
import com.monikamart.monikamart.entity.CartItem;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.exception.GlobalExceptionHandler;
import com.monikamart.monikamart.service.CartService;
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
public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetCartByUserId() throws Exception {
        User user = new User("Test User", "test@monikamart.com", "+1234567890", "password123", "123 St", "City", "State", "12345", "USER");
        user.setId(1L);
        Product product = new Product("Laptop", "Desc", 999.0, "img.jpg", "Electronics", 10, 4.5, 10);
        product.setId(1L);
        CartItem item = new CartItem(user, product, 2);
        item.setId(1L);

        when(cartService.getCartByUserId(1L)).thenReturn(Arrays.asList(item));

        mockMvc.perform(get("/api/cart/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].product.name").value("Laptop"));
    }

    @Test
    void testAddToCart() throws Exception {
        CartRequest request = new CartRequest(1L, 1L, 2);
        User user = new User();
        user.setId(1L);
        Product product = new Product("Laptop", "Desc", 999.0, "img.jpg", "Electronics", 10, 4.5, 10);
        product.setId(1L);
        CartItem item = new CartItem(user, product, 2);
        item.setId(1L);

        when(cartService.addToCart(any(CartRequest.class))).thenReturn(item);

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.quantity").value(2));
    }

    @Test
    void testUpdateQuantity() throws Exception {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setQuantity(5);

        when(cartService.updateCartItemQuantity(eq(1L), eq(5))).thenReturn(item);

        mockMvc.perform(put("/api/cart/1?quantity=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.quantity").value(5));
    }

    @Test
    void testRemoveCartItem() throws Exception {
        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
