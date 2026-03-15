package com.ordermgtsystem.dto;

import java.math.BigDecimal;

public record ProductViewResponse(
        Long productId,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {
}