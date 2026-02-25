package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.request.ProductRawMaterialRequest;
import com.autoflex.inventory.dto.request.ProductRequest;
import com.autoflex.inventory.dto.response.ProductRawMaterialResponse;
import com.autoflex.inventory.dto.response.ProductResponse;
import com.autoflex.inventory.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productRequest = new ProductRequest("Test Product", BigDecimal.TEN, Collections.emptyList());
        productResponse = new ProductResponse(productId, "Test Product", BigDecimal.TEN, Collections.emptyList());
    }

    @Test
    void findAll_ShouldReturnListOfProducts() throws Exception {
        when(productService.findAll()).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(productId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void findById_ShouldReturnProduct() throws Exception {
        when(productService.findById(productId)).thenReturn(productResponse);

        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void create_ShouldReturnCreatedProduct() throws Exception {
        when(productService.create(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void update_ShouldReturnUpdatedProduct() throws Exception {
        when(productService.update(eq(productId), any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void addRawMaterial_ShouldReturnCreated() throws Exception {
        UUID rawMaterialId = UUID.randomUUID();
        ProductRawMaterialRequest request = new ProductRawMaterialRequest(rawMaterialId, 5.0);
        ProductRawMaterialResponse response = new ProductRawMaterialResponse(UUID.randomUUID(), rawMaterialId, "Raw Material", 5.0);

        when(productService.addRawMaterial(eq(productId), any(ProductRawMaterialRequest.class))).thenReturn(response);

        mockMvc.perform(post("/products/{id}/raw-materials", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rawMaterialId").value(rawMaterialId.toString()));
    }

    @Test
    void removeRawMaterial_ShouldReturnNoContent() throws Exception {
        UUID productRawMaterialId = UUID.randomUUID();
        doNothing().when(productService).removeRawMaterial(productRawMaterialId);

        mockMvc.perform(delete("/products/raw-materials/{productRawMaterialId}", productRawMaterialId))
                .andExpect(status().isNoContent());
    }
}
