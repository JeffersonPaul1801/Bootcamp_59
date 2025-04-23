package com.microservice.service.impl;

import com.microservice.Dto.YankiResponse;
import com.microservice.model.Yanki;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface YankiServiceImpl {

    Flux<YankiResponse> getAllYanki();
    Flux<YankiResponse> getPhoneYanki(String phoneNumber);
    Mono<Yanki> createYanki(Yanki yanki);
//    String processYanki(Yanki yanki) throws InterruptedException;

    Mono<Yanki> transfer(String senderPhone, String receiverPhone, Double amount);

    Mono<Yanki> linkDebitCard(String phoneNumber, String debitCardNumber, Long numberAccount);
}
