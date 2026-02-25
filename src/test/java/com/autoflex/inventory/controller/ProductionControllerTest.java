package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.response.ProductionSuggestionResponse;
import com.autoflex.inventory.service.ProductionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductionController.class)
class ProductionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductionService productionService;

    @Test
    void getSuggestions_ShouldReturnListOfSuggestions() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductionSuggestionResponse suggestion = new ProductionSuggestionResponse(
                productId, "Product A", BigDecimal.TEN, 5
        );

        when(productionService.getSuggestions()).thenReturn(List.of(suggestion));

        mockMvc.perform(get("/production/suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$[0].productName").value("Product A"))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    void getSuggestions_ShouldReturnEmptyList_WhenNoSuggestions() throws Exception {
        when(productionService.getSuggestions()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/production/suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}
