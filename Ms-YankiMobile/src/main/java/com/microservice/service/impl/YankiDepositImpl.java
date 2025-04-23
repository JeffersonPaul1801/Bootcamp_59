package com.microservice.service.impl;

import com.microservice.Dto.YankiDepositResponse;
import com.microservice.model.YankiDeposit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface YankiDepositImpl {

    Flux<YankiDepositResponse> getAllYanki();
    Flux<YankiDepositResponse> getPhoneYanki(String phoneNumber);
    Mono<YankiDeposit> createYanki(YankiDeposit yankiDeposit);
}
