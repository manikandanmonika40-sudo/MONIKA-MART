package com.monikamart.monikamart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monikamart.monikamart.entity.Category;
import com.monikamart.monikamart.exception.GlobalExceptionHandler;
import com.monikamart.monikamart.service.CategoryService;
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
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category cat = new Category("Electronics", "Tech gadgets", "fa-laptop", "img.jpg");
        cat.setId(1L);

        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(cat));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void testGetCategoryById_Found() throws Exception {
        Category cat = new Category("Electronics", "Tech gadgets", "fa-laptop", "img.jpg");
        cat.setId(1L);

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(cat));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Electronics"));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        when(categoryService.getCategoryById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testCreateCategory() throws Exception {
        Category cat = new Category("Electronics", "Tech gadgets", "fa-laptop", "img.jpg");
        cat.setId(1L);

        when(categoryService.createCategory(any(Category.class))).thenReturn(cat);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Electronics"));
    }
}
