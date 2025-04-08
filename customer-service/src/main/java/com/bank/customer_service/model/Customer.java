package com.bank.customer_service.model;

import com.bank.customer_service.model.enums.CustomerType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

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
    private CustomerType customerType; // Identificar tipo de cliente (Personal, PYME, etc.)
    private boolean hasCreditCard; // Nueva propiedad para validar si tiene tarjeta de crédito

    // Agregar un campo adicional para clientes VIP si es necesario
    private BigDecimal requiredAverageBalance; // Solo para clientes VIP
}

