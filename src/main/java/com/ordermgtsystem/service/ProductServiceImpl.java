package com.ordermgtsystem.service;

import com.ordermgtsystem.domain.Product;
import com.ordermgtsystem.dto.ProductViewResponse;
import com.ordermgtsystem.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductViewResponse> getProductsForOrderPage() {
        return productRepository.findAllWithStock().stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductViewResponse toResponse(Product product) {
        int stockQuantity = product.getStock() == null ? 0 : product.getStock().getQuantity();
        return new ProductViewResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                stockQuantity
        );
    }
}