package com.ordermgtsystem.service;

import com.ordermgtsystem.dto.ProductViewResponse;

import java.util.List;

public interface ProductService {

    List<ProductViewResponse> getProductsForOrderPage();
}