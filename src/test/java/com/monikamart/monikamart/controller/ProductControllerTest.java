package com.monikamart.monikamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monikamart.monikamart.entity.Product;
import com.monikamart.monikamart.exception.GlobalExceptionHandler;
import com.monikamart.monikamart.service.ProductService;
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
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product p = new Product("MacBook", "Apple laptop", 1299.0, "img.jpg", "Electronics", 10, 4.8, 50);
        p.setId(1L);

        when(productService.getAllProducts(null, null, null, null, null)).thenReturn(Arrays.asList(p));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("MacBook"))
                .andExpect(jsonPath("$[0].price").value(1299.0));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        Product p = new Product("MacBook", "Apple laptop", 1299.0, "img.jpg", "Electronics", 10, 4.8, 50);
        p.setId(1L);

        when(productService.getProductById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MacBook"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        Product p = new Product("MacBook", "Apple laptop", 1299.0, "img.jpg", "Electronics", 10, 4.8, 50);
        p.setId(1L);

        when(productService.createProduct(any(Product.class))).thenReturn(p);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("MacBook"));
    }
}
