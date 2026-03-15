package com.ordermgtsystem.config;

import com.ordermgtsystem.domain.Product;
import com.ordermgtsystem.domain.Stock;
import com.ordermgtsystem.repository.ProductRepository;
import com.ordermgtsystem.repository.StockRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DemoDataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public DemoDataInitializer(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0) {
            return;
        }

        Product keyboard = productRepository.save(new Product("Mechanical Keyboard", BigDecimal.valueOf(89000)));
        Product mouse = productRepository.save(new Product("Wireless Mouse", BigDecimal.valueOf(42000)));
        Product monitor = productRepository.save(new Product("27inch Monitor", BigDecimal.valueOf(279000)));

        stockRepository.save(new Stock(keyboard, 5));
        stockRepository.save(new Stock(mouse, 10));
        stockRepository.save(new Stock(monitor, 2));
    }
}