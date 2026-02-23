package com.autoflex.inventory.dto.request;

public record RawMaterialRequest(
    String name,
    Double stockQuantity
) {
}
