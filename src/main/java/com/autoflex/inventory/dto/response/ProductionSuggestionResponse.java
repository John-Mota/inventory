package com.autoflex.inventory.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductionSuggestionResponse(
    UUID productId,
    String productName,
    BigDecimal productValue,
    int quantity
) {
}
