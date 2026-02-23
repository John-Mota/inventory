package com.autoflex.inventory.service.impl;

import com.autoflex.inventory.dto.request.RawMaterialRequest;
import com.autoflex.inventory.dto.response.RawMaterialResponse;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.RawMaterialRepository;
import com.autoflex.inventory.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    @Override
    public List<RawMaterialResponse> findAll() {
        return rawMaterialRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RawMaterialResponse findById(UUID id) {
        return toResponse(getRawMaterial(id));
    }

    @Override
    public RawMaterialResponse create(RawMaterialRequest request) {
        RawMaterial rawMaterial = RawMaterial.builder()
                .name(request.name())
                .stockQuantity(request.stockQuantity())
                .build();
        return toResponse(rawMaterialRepository.save(rawMaterial));
    }

    @Override
    public RawMaterialResponse update(UUID id, RawMaterialRequest request) {
        RawMaterial rawMaterial = getRawMaterial(id);
        rawMaterial.setName(request.name());
        rawMaterial.setStockQuantity(request.stockQuantity());
        return toResponse(rawMaterialRepository.save(rawMaterial));
    }

    @Override
    public void delete(UUID id) {
        rawMaterialRepository.deleteById(id);
    }

    private RawMaterial getRawMaterial(UUID id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matéria-prima não encontrada"));
    }

    private RawMaterialResponse toResponse(RawMaterial rawMaterial) {
        return new RawMaterialResponse(
                rawMaterial.getId(),
                rawMaterial.getName(),
                rawMaterial.getStockQuantity()
        );
    }
}
