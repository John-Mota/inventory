package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.response.ProductionSuggestionResponse;

import java.util.List;

public interface ProductionService {
    List<ProductionSuggestionResponse> getSuggestions();
}
