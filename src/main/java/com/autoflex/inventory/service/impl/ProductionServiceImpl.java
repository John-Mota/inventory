package com.autoflex.inventory.service.impl;

import com.autoflex.inventory.dto.response.ProductionSuggestionResponse;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import com.autoflex.inventory.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Override
    public List<ProductionSuggestionResponse> getSuggestions() {

        // Estoque atual em memória para simular consumo
        Map<UUID, Double> stock = new HashMap<>();
        rawMaterialRepository.findAll()
                .forEach(rm -> stock.put(rm.getId(), rm.getStockQuantity()));

        // Ordena produtos por maior valor (prioridade)
        List<Product> products = productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getValue).reversed())
                .toList();

        List<ProductionSuggestionResponse> suggestions = new ArrayList<>();

        for (Product product : products) {
            List<ProductRawMaterial> materials = product.getRawMaterials();

            if (materials == null || materials.isEmpty()) continue;

            // Calcula quantas unidades é possível produzir
            int quantity = materials.stream()
                    .mapToInt(prm -> {
                        Double available = stock.getOrDefault(prm.getRawMaterial().getId(), 0.0);
                        return (int) Math.floor(available / prm.getRequiredQuantity());
                    })
                    .min()
                    .orElse(0);

            if (quantity <= 0) continue;

            // Consome o estoque em memória
            for (ProductRawMaterial prm : materials) {
                UUID rmId = prm.getRawMaterial().getId();
                double consumed = prm.getRequiredQuantity() * quantity;
                stock.put(rmId, stock.get(rmId) - consumed);
            }

            suggestions.add(new ProductionSuggestionResponse(
                    product.getId(),
                    product.getName(),
                    product.getValue(),
                    quantity
            ));
        }

        return suggestions;
    }
}
