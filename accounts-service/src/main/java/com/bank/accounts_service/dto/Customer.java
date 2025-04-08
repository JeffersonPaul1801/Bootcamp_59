package com.bank.accounts_service.dto;

import lombok.Data;

@Data
public class Customer {
    private String id;
    private String profile; // Ejemplo: "VIP", "PYME", etc.
}