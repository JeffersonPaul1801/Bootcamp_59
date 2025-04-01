package com.bank.transactions_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    private String id;
    private String customerId;
    private String accountId;
    private String transactionType; // DEPOSIT, WITHDRAWAL, PAYMENT, PURCHASE
    private BigDecimal amount;
    private LocalDateTime timestamp;
}

