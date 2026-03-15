package com.ordermgtsystem.service;

import com.ordermgtsystem.domain.Order;
import com.ordermgtsystem.domain.Product;
import com.ordermgtsystem.domain.Stock;
import com.ordermgtsystem.dto.CreateOrderRequest;
import com.ordermgtsystem.dto.OrderResponse;
import com.ordermgtsystem.exception.OutOfStockException;
import com.ordermgtsystem.repository.OrderRepository;
import com.ordermgtsystem.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("keyboard", BigDecimal.valueOf(10000));
        ReflectionTestUtils.setField(product, "id", 10L);
    }

    @Test
    @DisplayName("placeOrder BVA: 재고가 1개이고 주문 수량이 1개면 주문에 성공한다")
    void createOrder_success_whenStockIsOne() {
        CreateOrderRequest request = new CreateOrderRequest(10L, 1);
        Stock stock = new Stock(product, 1);

        when(stockRepository.findByProductIdForUpdate(10L)).thenReturn(Optional.of(stock));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order, "id", 100L);
            return order;
        });

        OrderResponse response = orderService.createOrder(request);

        assertEquals(100L, response.orderId());
        assertEquals(10L, response.productId());
        assertEquals(1, response.quantity());
        assertEquals("PENDING", response.status());
        assertEquals(0, stock.getQuantity());

        verify(stockRepository).findByProductIdForUpdate(10L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("placeOrder BVA: 재고가 0개일 때 주문 수량이 1개면 OutOfStockException이 발생한다")
    void createOrder_fail_whenStockIsZero() {
        CreateOrderRequest request = new CreateOrderRequest(10L, 1);
        Stock stock = new Stock(product, 0);

        when(stockRepository.findByProductIdForUpdate(10L)).thenReturn(Optional.of(stock));

        OutOfStockException ex = assertThrows(
                OutOfStockException.class,
                () -> orderService.createOrder(request)
        );

        assertTrue(ex.getMessage().contains("재고가 부족"));
        verify(stockRepository).findByProductIdForUpdate(10L);
        verify(orderRepository, never()).save(any(Order.class));
    }
}