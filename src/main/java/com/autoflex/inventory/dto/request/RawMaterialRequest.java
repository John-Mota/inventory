package com.autoflex.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RawMaterialRequest(
    @NotBlank(message = "O nome da matéria-prima é obrigatório")
    String name,

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @PositiveOrZero(message = "A quantidade em estoque deve ser zero ou positiva")
    Double stockQuantity
) {
}
