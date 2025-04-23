package com.microservice.service;

import com.microservice.Dto.YankiDepositResponse;
import com.microservice.model.YankiDeposit;
import com.microservice.repository.YankiDepositRepository;
import com.microservice.service.impl.YankiDepositImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class YankiDepositService implements YankiDepositImpl {

    @Autowired
    YankiDepositRepository depositRepository;

    @Override
    public Flux<YankiDepositResponse> getAllYanki() {
        return depositRepository.findAll().map(yankiDeposit -> YankiDepositResponse.builder().amount(yankiDeposit.getAmount())
                .identityDocument(yankiDeposit.getIdentityDocument())
                .phoneNumber(yankiDeposit.getPhoneNumber())
                .phoneNumberDestination(yankiDeposit.getPhoneNumberDestination())
                .build());
    }

    @Override
    public Flux<YankiDepositResponse> getPhoneYanki(String phoneNumber) {
        return depositRepository.findAll().filter(yankiDeposit -> yankiDeposit.getPhoneNumber().equals(phoneNumber))
                .map(yankiDeposit -> YankiDepositResponse.builder().amount(yankiDeposit.getAmount())
                        .identityDocument(yankiDeposit.getIdentityDocument())
                        .phoneNumber(yankiDeposit.getPhoneNumber())
                        .phoneNumberDestination(yankiDeposit.getPhoneNumberDestination())
                        .build());
    }

    @Override
    public Mono<YankiDeposit> createYanki(YankiDeposit yankiDeposit) {
        return depositRepository.save(yankiDeposit);
    }
}
