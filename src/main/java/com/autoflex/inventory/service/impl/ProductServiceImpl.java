package com.autoflex.inventory.service.impl;

import com.autoflex.inventory.dto.request.ProductRawMaterialRequest;
import com.autoflex.inventory.dto.request.ProductRequest;
import com.autoflex.inventory.dto.response.ProductRawMaterialResponse;
import com.autoflex.inventory.dto.response.ProductResponse;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.service.ProductService;
import com.autoflex.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(UUID id) {
        return toResponse(getProduct(id));
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .value(request.value())
                .build();
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = getProduct(id);
        product.setName(request.name());
        product.setValue(request.value());
        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductRawMaterialResponse addRawMaterial(UUID productId, ProductRawMaterialRequest request) {
        Product product = getProduct(productId);
        RawMaterial rawMaterial = rawMaterialRepository.findById(request.rawMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found"));

        ProductRawMaterial prm = ProductRawMaterial.builder()
                .product(product)
                .rawMaterial(rawMaterial)
                .requiredQuantity(request.requiredQuantity())
                .build();

        return toRawMaterialResponse(productRawMaterialRepository.save(prm));
    }

    @Override
    public void removeRawMaterial(UUID productRawMaterialId) {
        productRawMaterialRepository.deleteById(productRawMaterialId);
    }

    private Product getProduct(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private ProductResponse toResponse(Product product) {
        List<ProductRawMaterialResponse> rawMaterials = productRawMaterialRepository
                .findByProductId(product.getId()).stream()
                .map(this::toRawMaterialResponse)
                .toList();
        return new ProductResponse(product.getId(), product.getName(), product.getValue(), rawMaterials);
    }

    private ProductRawMaterialResponse toRawMaterialResponse(ProductRawMaterial prm) {
        return new ProductRawMaterialResponse(
                prm.getId(),
                prm.getRawMaterial().getId(),
                prm.getRawMaterial().getName(),
                prm.getRequiredQuantity()
        );
    }
}
