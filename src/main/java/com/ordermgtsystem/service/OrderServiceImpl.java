package com.ordermgtsystem.service;

import com.ordermgtsystem.domain.Order;
import com.ordermgtsystem.domain.Stock;
import com.ordermgtsystem.dto.CreateOrderRequest;
import com.ordermgtsystem.dto.OrderResponse;
import com.ordermgtsystem.exception.NotFoundException;
import com.ordermgtsystem.repository.OrderRepository;
import com.ordermgtsystem.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderServiceImpl(OrderRepository orderRepository, StockRepository stockRepository) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Stock stock = findStockWithPessimisticLock(request.productId());
        int orderQuantity = request.quantity();

        stock.decrease(orderQuantity);

        Order savedOrder = orderRepository.save(Order.create(stock.getProduct(), orderQuantity));
        return toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다. orderId=" + orderId));
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProduct().getId(),
                order.getQuantity(),
                order.getStatus().name(),
                order.getOrderedAt()
        );
    }

    private Stock findStockWithPessimisticLock(Long productId) {
        return stockRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException("상품 재고를 찾을 수 없습니다. productId=" + productId));
    }
}