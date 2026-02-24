package com.autoflex.inventory.dto.request;

import com.autoflex.inventory.dto.response.ProductRawMaterialResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
    @NotBlank(message = "O nome do produto é obrigatório")
    String name,

    @NotNull(message = "O valor do produto é obrigatório")
    @Positive(message = "O valor do produto deve ser positivo")
    BigDecimal value,

    List<ProductRawMaterialResponse>rawMaterials
) {
}
