package com.autoflex.inventory.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ProductRawMaterialRequest(
    @NotNull(message = "O ID da matéria-prima é obrigatório")
    UUID rawMaterialId,

    @NotNull(message = "A quantidade necessária é obrigatória")
    @Positive(message = "A quantidade necessária deve ser positiva")
    Double requiredQuantity
) {
}
