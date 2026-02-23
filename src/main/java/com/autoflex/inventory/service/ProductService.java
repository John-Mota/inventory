package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.request.ProductRawMaterialRequest;
import com.autoflex.inventory.dto.request.ProductRequest;
import com.autoflex.inventory.dto.response.ProductRawMaterialResponse;
import com.autoflex.inventory.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(UUID id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(UUID id, ProductRequest request);
    void delete(UUID id);
    ProductRawMaterialResponse addRawMaterial(UUID productId, ProductRawMaterialRequest request);
    void removeRawMaterial(UUID productRawMaterialId);
}
