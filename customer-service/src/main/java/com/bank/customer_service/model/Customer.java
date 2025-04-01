package com.bank.customer_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    private String id;
    private String name;
    private String documentNumber;
    private String customerType; // PERSONAL o EMPRESARIAL
}

