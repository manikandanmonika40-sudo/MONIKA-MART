package com.monikamart.monikamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monikamart.monikamart.dto.WishlistRequest;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.entity.User;
import com.monikamart.monikamart.entity.WishlistItem;
import com.monikamart.monikamart.exception.GlobalExceptionHandler;
import com.monikamart.monikamart.service.WishlistService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WishlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(wishlistController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetWishlistByUserId() throws Exception {
        User user = new User();
        user.setId(1L);
        Product product = new Product("Watch", "Desc", 199.0, "img.jpg", "Accessories", 5, 4.7, 12);
        product.setId(2L);
        WishlistItem item = new WishlistItem(user, product);
        item.setId(1L);

        when(wishlistService.getWishlistByUserId(1L)).thenReturn(Arrays.asList(item));

        mockMvc.perform(get("/api/wishlist/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product.name").value("Watch"));
    }

    @Test
    void testAddToWishlist() throws Exception {
        WishlistRequest req = new WishlistRequest(1L, 2L);
        User user = new User();
        user.setId(1L);
        Product product = new Product("Watch", "Desc", 199.0, "img.jpg", "Accessories", 5, 4.7, 12);
        product.setId(2L);
        WishlistItem item = new WishlistItem(user, product);
        item.setId(1L);

        when(wishlistService.addToWishlist(1L, 2L)).thenReturn(item);

        mockMvc.perform(post("/api/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.product.name").value("Watch"));
    }

    @Test
    void testRemoveFromWishlist() throws Exception {
        mockMvc.perform(delete("/api/wishlist/user/1/product/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
