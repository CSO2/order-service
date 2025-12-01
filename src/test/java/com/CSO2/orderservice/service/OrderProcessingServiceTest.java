package com.CSO2.orderservice.service;

import com.CSO2.orderservice.client.CartClient;
import com.CSO2.orderservice.client.CatalogClient;
import com.CSO2.orderservice.dto.request.CheckoutRequest;
import com.CSO2.orderservice.dto.response.CartDTO;
import com.CSO2.orderservice.dto.response.CartItemDTO;
import com.CSO2.orderservice.dto.response.OrderDetailDTO;
import com.CSO2.orderservice.dto.response.OrderSummaryDTO;
import com.CSO2.orderservice.entity.Order;
import com.CSO2.orderservice.entity.ShippingAddress;
import com.CSO2.orderservice.repository.OrderRepository;
import com.CSO2.orderservice.service.shipping.ShippingStrategy;
import com.CSO2.orderservice.service.shipping.ShippingStrategyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessingServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartClient cartClient;
    @Mock
    private CatalogClient catalogClient;
    @Mock
    private ShippingStrategyFactory shippingStrategyFactory;
    @Mock
    private ShippingStrategy shippingStrategy;

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    @Test
    void createOrder_Success() {
        // Setup
        String userId = "user1";
        CheckoutRequest req = new CheckoutRequest();
        req.setShippingMethod("standard");
        ShippingAddress address = new ShippingAddress();
        req.setShippingAddress(address);

        CartDTO cart = new CartDTO();
        CartItemDTO item = new CartItemDTO();
        item.setProductId("p1");
        item.setQuantity(1);
        item.setPrice(new BigDecimal("100"));
        cart.setItems(List.of(item));
        cart.setTotalValue(new BigDecimal("100"));

        when(cartClient.getCart(userId)).thenReturn(cart);
        when(catalogClient.validateStock(any())).thenReturn(Collections.emptyList());
        when(shippingStrategyFactory.getStrategy("standard")).thenReturn(shippingStrategy);
        when(shippingStrategy.calculateCost(any())).thenReturn(new BigDecimal("10"));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Order result = orderProcessingService.createOrder(userId, req);

        // Verify
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(Order.OrderStatus.PENDING, result.getStatus());
        verify(catalogClient).reduceStock(any());
        verify(cartClient).clearCart(userId);
    }

    @Test
    void createOrder_CartEmpty() {
        String userId = "user1";
        CheckoutRequest req = new CheckoutRequest();
        when(cartClient.getCart(userId)).thenReturn(new CartDTO());

        assertThrows(RuntimeException.class, () -> orderProcessingService.createOrder(userId, req));
    }

    @Test
    void createOrder_OutOfStock() {
        String userId = "user1";
        CheckoutRequest req = new CheckoutRequest();
        CartDTO cart = new CartDTO();
        CartItemDTO item = new CartItemDTO();
        item.setProductId("p1");
        item.setQuantity(1);
        cart.setItems(List.of(item));
        when(cartClient.getCart(userId)).thenReturn(cart);
        when(catalogClient.validateStock(any())).thenReturn(List.of("p1"));

        assertThrows(RuntimeException.class, () -> orderProcessingService.createOrder(userId, req));
    }

    @Test
    void getUserOrders() {
        String userId = "user1";
        Order order = new Order();
        order.setId("o1");
        order.setUserId(userId);
        order.setTotalAmount(new BigDecimal("100"));
        order.setStatus(Order.OrderStatus.PENDING);
        when(orderRepository.findByUserId(userId)).thenReturn(List.of(order));

        List<OrderSummaryDTO> result = orderProcessingService.getUserOrders(userId);

        assertEquals(1, result.size());
        assertEquals("o1", result.get(0).getId());
    }

    @Test
    void getOrderDetails_Found() {
        String orderId = "o1";
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderDetailDTO result = orderProcessingService.getOrderDetails(orderId);

        assertEquals(orderId, result.getId());
    }

    @Test
    void getOrderDetails_NotFound() {
        String orderId = "o1";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderProcessingService.getOrderDetails(orderId));
    }

    @Test
    void updateStatus() {
        String orderId = "o1";
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderProcessingService.updateStatus(orderId, "SHIPPED");

        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
        verify(orderRepository).save(order);
    }
}
