package com.autoflex.inventory.dto.request;

import java.math.BigDecimal;

public record ProductRequest(
    String name,
    BigDecimal value
) {
}
