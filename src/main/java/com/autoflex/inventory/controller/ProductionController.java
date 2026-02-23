package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.response.ProductionSuggestionResponse;
import com.autoflex.inventory.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<ProductionSuggestionResponse>> getSuggestions() {
        return ResponseEntity.ok(productionService.getSuggestions());
    }
}
