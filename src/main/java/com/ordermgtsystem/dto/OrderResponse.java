package com.ordermgtsystem.dto;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        Long productId,
        Integer quantity,
        String status,
        LocalDateTime orderedAt
) {
}