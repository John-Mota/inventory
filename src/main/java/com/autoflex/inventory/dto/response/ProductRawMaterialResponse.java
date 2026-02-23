package com.autoflex.inventory.dto.response;

import java.util.UUID;

public record ProductRawMaterialResponse(
    UUID id,
    UUID rawMaterialId,
    String rawMaterialName,
    Double requiredQuantity
) {
}
