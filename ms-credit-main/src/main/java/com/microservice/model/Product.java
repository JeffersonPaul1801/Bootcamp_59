package com.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String customerId;  // Cliente al que pertenece el producto
    private ProductType type;   // Tipo de producto (CUENTA o CREDITO)
    private Double balance;     // Saldo actual o deuda pendiente
    private Double limitAmount; // Solo para tarjetas de crédito o préstamos
    private String status;
}


