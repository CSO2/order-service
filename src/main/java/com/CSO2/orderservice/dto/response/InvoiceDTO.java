package com.CSO2.orderservice.dto.response;

import lombok.Data;

@Data
public class InvoiceDTO {
    private String pdfUrl;
    private String billingDetails;
}
