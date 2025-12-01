package com.CSO2.orderservice.controller;

import com.CSO2.orderservice.service.OrderProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminOrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderProcessingService orderProcessingService;

    @InjectMocks
    private AdminOrderController adminOrderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminOrderController).build();
    }

    @Test
    void updateOrderStatus() throws Exception {
        mockMvc.perform(put("/api/admin/orders/o1/status")
                .param("status", "SHIPPED"))
                .andExpect(status().isOk());

        verify(orderProcessingService).updateStatus("o1", "SHIPPED");
    }
}
