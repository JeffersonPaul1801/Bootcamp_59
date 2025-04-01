package com.bank.credits_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "credits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {
    @Id
    private String id;
    private String customerId; // Relación con Customer
    private String creditType; // PERSONAL, BUSINESS, CREDIT_CARD
    private BigDecimal amount; // Monto total del crédito
    private BigDecimal balance; // Saldo disponible
    private BigDecimal creditLimit; // Para tarjetas de crédito
    private boolean isActive; // Estado del crédito
}

