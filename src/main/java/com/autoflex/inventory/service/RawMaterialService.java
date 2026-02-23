package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.request.RawMaterialRequest;
import com.autoflex.inventory.dto.response.RawMaterialResponse;

import java.util.List;
import java.util.UUID;

public interface RawMaterialService {
    List<RawMaterialResponse> findAll();
    RawMaterialResponse findById(UUID id);
    RawMaterialResponse create(RawMaterialRequest request);
    RawMaterialResponse update(UUID id, RawMaterialRequest request);
    void delete(UUID id);
}
