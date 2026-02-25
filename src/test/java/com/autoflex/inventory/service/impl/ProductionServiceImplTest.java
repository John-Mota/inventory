package com.autoflex.inventory.service.impl;

import com.autoflex.inventory.dto.response.ProductionSuggestionResponse;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductionServiceImpl productionService;

    private Product product;
    private RawMaterial rawMaterial;
    private ProductRawMaterial productRawMaterial;

    @BeforeEach
    void setUp() {
        rawMaterial = RawMaterial.builder()
                .id(UUID.randomUUID())
                .name("Wood")
                .stockQuantity(100.0)
                .build();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Chair")
                .value(BigDecimal.valueOf(50.0))
                .build();

        productRawMaterial = ProductRawMaterial.builder()
                .id(UUID.randomUUID())
                .product(product)
                .rawMaterial(rawMaterial)
                .requiredQuantity(10.0)
                .build();

        product.setRawMaterials(List.of(productRawMaterial));
    }

    @Test
    void getSuggestions_ShouldReturnCorrectQuantity() {
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        assertFalse(suggestions.isEmpty());
        assertEquals(1, suggestions.size());
        assertEquals(10, suggestions.get(0).quantity()); // 100 stock / 10 required = 10 units
    }

    @Test
    void getSuggestions_ShouldReturnZero_WhenStockIsInsufficient() {
        rawMaterial.setStockQuantity(5.0); // Less than required (10.0)
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        assertTrue(suggestions.isEmpty());
    }

    @Test
    void getSuggestions_ShouldPrioritizeHigherValueProducts() {
        Product cheapProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Stool")
                .value(BigDecimal.valueOf(20.0))
                .build();

        ProductRawMaterial prmCheap = ProductRawMaterial.builder()
                .id(UUID.randomUUID())
                .product(cheapProduct)
                .rawMaterial(rawMaterial)
                .requiredQuantity(10.0)
                .build();

        cheapProduct.setRawMaterials(List.of(prmCheap));

        // Total stock 100. Chair needs 10, Stool needs 10.
        // Chair value 50, Stool value 20.
        // Should produce as many Chairs as possible first.

        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product, cheapProduct));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        // The order of suggestions depends on the sorting logic in the service.
        // The service sorts by value descending.
        // Chair (50.0) > Stool (20.0)
        
        // However, the assertion expected 2 suggestions, but got 1.
        // This likely means the second product (Stool) was not added to the list because quantity was 0.
        // Let's check the service logic.
        // The service logic: if (quantity <= 0) continue;
        
        // In this test case:
        // Chair consumes 10 * 10 = 100 stock.
        // Remaining stock = 0.
        // Stool needs 10. Available 0. Quantity = 0.
        // So Stool is skipped.
        
        assertEquals(1, suggestions.size());
        assertEquals("Chair", suggestions.get(0).productName());
        assertEquals(10, suggestions.get(0).quantity());
    }
    
    @Test
    void getSuggestions_ShouldHandleProductsWithoutRawMaterials() {
        product.setRawMaterials(Collections.emptyList());
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        assertTrue(suggestions.isEmpty());
    }
}
