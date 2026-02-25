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
        // Deve calcular corretamente a quantidade de produtos que podem ser produzidos com base no estoque
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        assertFalse(suggestions.isEmpty());
        assertEquals(1, suggestions.size());
        assertEquals(10, suggestions.get(0).quantity()); // 100 estoque / 10 necessário = 10 unidades
    }

    @Test
    void getSuggestions_ShouldReturnZero_WhenStockIsInsufficient() {
        // Não deve retornar sugestões quando o estoque for insuficiente para produzir sequer uma unidade
        rawMaterial.setStockQuantity(5.0); // Menos que o necessário (10.0)
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        assertTrue(suggestions.isEmpty());
    }

    @Test
    void getSuggestions_ShouldPrioritizeHigherValueProducts() {
        // Deve priorizar a produção de produtos com maior valor, consumindo o estoque disponível
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

        // Estoque total 100. Cadeira precisa de 10, Banqueta precisa de 10.
        // Valor da Cadeira 50, Valor da Banqueta 20.
        // Deve produzir o máximo de Cadeiras possível primeiro.

        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product, cheapProduct));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        // A ordem das sugestões depende da lógica de ordenação no serviço.
        // O serviço ordena por valor decrescente.
        // Cadeira (50.0) > Banqueta (20.0)
        
        // Neste caso de teste:
        // Cadeira consome 10 * 10 = 100 de estoque.
        // Estoque restante = 0.
        // Banqueta precisa de 10. Disponível 0. Quantidade = 0.
        // Então a Banqueta é ignorada.
        
        assertEquals(1, suggestions.size());
        assertEquals("Chair", suggestions.get(0).productName());
        assertEquals(10, suggestions.get(0).quantity());
    }
    
    @Test
    void getSuggestions_ShouldHandleProductsWithoutRawMaterials() {
        // Deve lidar corretamente com produtos que não possuem matérias-primas cadastradas (não sugerir produção)
        product.setRawMaterials(Collections.emptyList());
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductionSuggestionResponse> suggestions = productionService.getSuggestions();

        assertTrue(suggestions.isEmpty());
    }
}
