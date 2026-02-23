package com.autoflex.inventory.dto.response;

import java.util.UUID;

public record RawMaterialResponse(
    UUID id,
    String name,
    Double stockQuantity
) {
}
