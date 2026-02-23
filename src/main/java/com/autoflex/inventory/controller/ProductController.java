package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.request.ProductRawMaterialRequest;
import com.autoflex.inventory.dto.request.ProductRequest;
import com.autoflex.inventory.dto.response.ProductRawMaterialResponse;
import com.autoflex.inventory.dto.response.ProductResponse;
import com.autoflex.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/raw-materials")
    public ResponseEntity<ProductRawMaterialResponse> addRawMaterial(
            @PathVariable UUID id,
            @RequestBody ProductRawMaterialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addRawMaterial(id, request));
    }

    @DeleteMapping("/raw-materials/{productRawMaterialId}")
    public ResponseEntity<Void> removeRawMaterial(@PathVariable UUID productRawMaterialId) {
        productService.removeRawMaterial(productRawMaterialId);
        return ResponseEntity.noContent().build();
    }
}
