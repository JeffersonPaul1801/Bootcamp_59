package com.microservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YankiDepositResponse {
    private Double amount;
    private Long identityDocument;
    private String phoneNumber;
    private Long phoneNumberDestination;
}
