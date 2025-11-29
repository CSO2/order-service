package com.CSO2.orderservice.service;

import com.CSO2.orderservice.client.CartClient;
import com.CSO2.orderservice.client.CatalogClient;
import com.CSO2.orderservice.dto.request.CheckoutRequest;
import com.CSO2.orderservice.dto.request.StockCheckItem;
import com.CSO2.orderservice.dto.response.CartDTO;
import com.CSO2.orderservice.dto.response.OrderDetailDTO;
import com.CSO2.orderservice.dto.response.OrderSummaryDTO;
import com.CSO2.orderservice.entity.Order;
import com.CSO2.orderservice.entity.OrderItem;
import com.CSO2.orderservice.repository.OrderRepository;
import com.CSO2.orderservice.service.shipping.ShippingStrategy;
import com.CSO2.orderservice.service.shipping.ShippingStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final CatalogClient catalogClient;
    private final ShippingStrategyFactory shippingStrategyFactory;

    @Transactional
    public Order createOrder(String userId, CheckoutRequest req) {
        // 1. Fetch Cart from Cart Service
        CartDTO cart = cartClient.getCart(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Validate Stock with Catalog Service
        List<StockCheckItem> stockCheckItems = cart.getItems().stream()
                .map(item -> new StockCheckItem(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        List<String> outOfStockItems = catalogClient.validateStock(stockCheckItems);
        if (!outOfStockItems.isEmpty()) {
            throw new RuntimeException("Items out of stock: " + outOfStockItems);
        }

        // 3. Process Payment (Mocked)
        // paymentService.processPayment(req.getPaymentToken(), cart.getTotalAmount());

        // 4. Save Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(cart.getTotalValue());

        // Calculate tax and shipping
        BigDecimal tax = cart.getTotalValue().multiply(new BigDecimal("0.1"));
        order.setTaxAmount(tax);

        // Use the Strategy pattern to calculate shipping cost
        ShippingStrategy shippingStrategy = shippingStrategyFactory.getStrategy(req.getShippingMethod());
        order.setShippingCost(shippingStrategy.calculateCost(order));

        // Address from request
        order.setAddress(req.getShippingAddress());

        // Save Order Items
        List<OrderItem> items = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getPrice());
            return orderItem; // Note: OrderItem needs to be linked to Order if using JPA bidirectional, but
                              // usually uni-directional from Order is fine or setOrder(order)
        }).collect(Collectors.toList());

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        // 5. Reduce Stock
        catalogClient.reduceStock(stockCheckItems);

        // 6. Clear Cart
        cartClient.clearCart(userId);

        // 7. Publish OrderCreatedEvent (Mocked)
        // kafkaTemplate.send("order-created", order);

        return savedOrder;
    }

    public List<OrderSummaryDTO> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<OrderSummaryDTO> getRecentOrders() {
        return orderRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<OrderSummaryDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    public OrderDetailDTO getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToDetailDTO(order);
    }

    public void updateStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(Order.OrderStatus.valueOf(status));
        orderRepository.save(order);
    }

    private OrderSummaryDTO mapToSummaryDTO(Order order) {
        OrderSummaryDTO dto = new OrderSummaryDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotal(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setDate(order.getCreatedAt());
        return dto;
    }

    private OrderDetailDTO mapToDetailDTO(Order order) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(order.getId());
        dto.setItems(order.getItems());
        dto.setAddress(order.getAddress());
        // Populate timeline based on status history if available
        return dto;
    }
}
