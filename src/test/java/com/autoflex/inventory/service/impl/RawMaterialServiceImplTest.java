package com.autoflex.inventory.service.impl;

import com.autoflex.inventory.dto.request.RawMaterialRequest;
import com.autoflex.inventory.dto.response.RawMaterialResponse;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceImplTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private RawMaterialServiceImpl rawMaterialService;

    private RawMaterial rawMaterial;
    private RawMaterialRequest rawMaterialRequest;
    private UUID rawMaterialId;

    @BeforeEach
    void setUp() {
        rawMaterialId = UUID.randomUUID();
        rawMaterial = RawMaterial.builder()
                .id(rawMaterialId)
                .name("Test Material")
                .stockQuantity(100.0)
                .build();

        rawMaterialRequest = new RawMaterialRequest("Test Material", 100.0);
    }

    @Test
    void findAll_ShouldReturnListOfRawMaterials() {
        // Deve retornar uma lista de todas as matérias-primas cadastradas
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial));

        List<RawMaterialResponse> result = rawMaterialService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(rawMaterial.getName(), result.get(0).name());
    }

    @Test
    void findById_ShouldReturnRawMaterial_WhenExists() {
        // Deve retornar uma matéria-prima específica quando o ID for encontrado
        when(rawMaterialRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));

        RawMaterialResponse result = rawMaterialService.findById(rawMaterialId);

        assertNotNull(result);
        assertEquals(rawMaterial.getName(), result.name());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        // Deve lançar ResourceNotFoundException quando o ID da matéria-prima não for encontrado
        when(rawMaterialRepository.findById(rawMaterialId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.findById(rawMaterialId));
    }

    @Test
    void create_ShouldReturnCreatedRawMaterial() {
        // Deve criar e retornar a nova matéria-prima salva
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        RawMaterialResponse result = rawMaterialService.create(rawMaterialRequest);

        assertNotNull(result);
        assertEquals(rawMaterial.getName(), result.name());
    }

    @Test
    void update_ShouldReturnUpdatedRawMaterial() {
        // Deve atualizar e retornar a matéria-prima modificada
        when(rawMaterialRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        RawMaterialResponse result = rawMaterialService.update(rawMaterialId, rawMaterialRequest);

        assertNotNull(result);
        assertEquals(rawMaterial.getName(), result.name());
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Deve chamar o método deleteById do repositório para remover a matéria-prima
        doNothing().when(rawMaterialRepository).deleteById(rawMaterialId);

        rawMaterialService.delete(rawMaterialId);

        verify(rawMaterialRepository, times(1)).deleteById(rawMaterialId);
    }
}
