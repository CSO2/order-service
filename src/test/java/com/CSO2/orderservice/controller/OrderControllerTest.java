package com.CSO2.orderservice.controller;

import com.CSO2.orderservice.dto.request.CheckoutRequest;
import com.CSO2.orderservice.dto.response.OrderDetailDTO;
import com.CSO2.orderservice.dto.response.OrderSummaryDTO;
import com.CSO2.orderservice.entity.Order;
import com.CSO2.orderservice.service.OrderProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderProcessingService orderProcessingService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrder() throws Exception {
        CheckoutRequest req = new CheckoutRequest();
        when(orderProcessingService.createOrder(eq("user1"), any())).thenReturn(new Order());

        mockMvc.perform(post("/api/orders")
                .header("X-User-Id", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserOrders() throws Exception {
        when(orderProcessingService.getUserOrders("user1")).thenReturn(List.of(new OrderSummaryDTO()));

        mockMvc.perform(get("/api/orders")
                .header("X-User-Id", "user1"))
                .andExpect(status().isOk());
    }

    @Test
    void getRecentOrders() throws Exception {
        when(orderProcessingService.getRecentOrders()).thenReturn(List.of(new OrderSummaryDTO()));

        mockMvc.perform(get("/api/orders/recent"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllOrders() throws Exception {
        when(orderProcessingService.getAllOrders()).thenReturn(List.of(new OrderSummaryDTO()));

        mockMvc.perform(get("/api/orders/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderDetails() throws Exception {
        when(orderProcessingService.getOrderDetails("o1")).thenReturn(new OrderDetailDTO());

        mockMvc.perform(get("/api/orders/o1"))
                .andExpect(status().isOk());
    }
}
