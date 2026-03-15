package com.ordermgtsystem.service;

import com.ordermgtsystem.dto.CreateOrderRequest;
import com.ordermgtsystem.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrder(Long orderId);
}