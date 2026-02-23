package com.autoflex.inventory.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    BigDecimal value,
    List<ProductRawMaterialResponse> rawMaterials
) {
}
