package com.microservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "yankiDeposit")
@Builder
public class YankiDeposit {
    @Id
    private String id;
    private Double amount;
    private Long identityDocument;
    private String phoneNumber;
    private String email;
    private String imeiNumber;
    @JsonFormat(pattern="dd-MM-yyyy hh:mm:ss", timezone="GMT-05:00")
    private Date dateCreation = new Date();
    private Long phoneNumberDestination;
    private String idAccount;
}
