package com.bank.customer_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String fromCustomerId;
    private String fromAccountId;
    private String toAccountId;
    private String toCustomerId; // Agregado
    private BigDecimal amount;
}

