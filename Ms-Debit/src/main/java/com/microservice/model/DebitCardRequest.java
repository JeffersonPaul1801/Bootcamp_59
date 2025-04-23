package com.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitCardRequest {
    private String customerId;
    private String accountId;
    private String cardNumber;
    private String cvv;
    private List<String> accountIds;
    private String primaryAccountId;
    private LocalDate expirationDate;


}
