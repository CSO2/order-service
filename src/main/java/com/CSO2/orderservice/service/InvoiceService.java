package com.CSO2.orderservice.service;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    public byte[] generateInvoice(String orderId) {
        // Generates PDF using JasperReports or PDFBox.
        // Returning dummy byte array for now.
        return new byte[0];
    }
}
