package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.request.RawMaterialRequest;
import com.autoflex.inventory.dto.response.RawMaterialResponse;
import com.autoflex.inventory.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public ResponseEntity<List<RawMaterialResponse>> findAll() {
        return ResponseEntity.ok(rawMaterialService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RawMaterialResponse> create(@RequestBody RawMaterialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMaterialService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> update(@PathVariable UUID id, @RequestBody RawMaterialRequest request) {
        return ResponseEntity.ok(rawMaterialService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
