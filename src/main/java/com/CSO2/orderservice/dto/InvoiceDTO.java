package com.CSO2.orderservice.dto;

import lombok.Data;

@Data
public class InvoiceDTO {
    private String pdfUrl;
    private String billingDetails;
}
