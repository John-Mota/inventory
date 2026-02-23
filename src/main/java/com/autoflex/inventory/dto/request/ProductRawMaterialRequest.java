package com.autoflex.inventory.dto.request;

import java.util.UUID;

public record ProductRawMaterialRequest(
    UUID rawMaterialId,
    Double requiredQuantity
) {
}
