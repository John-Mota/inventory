package com.autoflex.inventory.service.impl;

import com.autoflex.inventory.dto.request.ProductRawMaterialRequest;
import com.autoflex.inventory.dto.request.ProductRequest;
import com.autoflex.inventory.dto.response.ProductRawMaterialResponse;
import com.autoflex.inventory.dto.response.ProductResponse;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private ProductRawMaterialRepository productRawMaterialRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .value(BigDecimal.TEN)
                .rawMaterials(Collections.emptyList())
                .build();

        productRequest = new ProductRequest("Test Product", BigDecimal.TEN, Collections.emptyList());
    }

    @Test
    void findAll_ShouldReturnListOfProducts() {
        // Deve retornar uma lista de produtos quando existirem no banco de dados
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productRawMaterialRepository.findByProductId(productId)).thenReturn(Collections.emptyList());

        List<ProductResponse> result = productService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).name());
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        // Deve retornar um produto específico quando o ID existir
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRawMaterialRepository.findByProductId(productId)).thenReturn(Collections.emptyList());

        ProductResponse result = productService.findById(productId);

        assertNotNull(result);
        assertEquals(product.getName(), result.name());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        // Deve lançar exceção ResourceNotFoundException quando o produto não for encontrado
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(productId));
    }

    @Test
    void create_ShouldReturnCreatedProduct() {
        // Deve criar e retornar o produto salvo
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRawMaterialRepository.findByProductId(productId)).thenReturn(Collections.emptyList());

        ProductResponse result = productService.create(productRequest);

        assertNotNull(result);
        assertEquals(product.getName(), result.name());
    }

    @Test
    void update_ShouldReturnUpdatedProduct() {
        // Deve atualizar e retornar o produto modificado
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRawMaterialRepository.findByProductId(productId)).thenReturn(Collections.emptyList());

        ProductResponse result = productService.update(productId, productRequest);

        assertNotNull(result);
        assertEquals(product.getName(), result.name());
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Deve chamar o método deleteById do repositório
        doNothing().when(productRepository).deleteById(productId);

        productService.delete(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void addRawMaterial_ShouldReturnResponse() {
        // Deve adicionar uma matéria-prima ao produto e retornar a resposta correspondente
        UUID rawMaterialId = UUID.randomUUID();
        RawMaterial rawMaterial = RawMaterial.builder().id(rawMaterialId).name("Raw Material").stockQuantity(100.0).build();
        ProductRawMaterialRequest request = new ProductRawMaterialRequest(rawMaterialId, 5.0);
        ProductRawMaterial prm = ProductRawMaterial.builder()
                .id(UUID.randomUUID())
                .product(product)
                .rawMaterial(rawMaterial)
                .requiredQuantity(5.0)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));
        when(productRawMaterialRepository.save(any(ProductRawMaterial.class))).thenReturn(prm);

        ProductRawMaterialResponse result = productService.addRawMaterial(productId, request);

        assertNotNull(result);
        assertEquals(rawMaterialId, result.rawMaterialId());
    }

    @Test
    void removeRawMaterial_ShouldCallRepositoryDelete() {
        // Deve chamar o método deleteById do repositório de matérias-primas do produto
        UUID prmId = UUID.randomUUID();
        doNothing().when(productRawMaterialRepository).deleteById(prmId);

        productService.removeRawMaterial(prmId);

        verify(productRawMaterialRepository, times(1)).deleteById(prmId);
    }
}
