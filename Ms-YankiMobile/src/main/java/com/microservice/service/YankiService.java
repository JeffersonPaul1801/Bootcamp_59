package com.microservice.service;

import com.microservice.Dto.YankiResponse;
import com.microservice.model.Yanki;
import com.microservice.repository.YankiRepository;
import com.microservice.service.impl.YankiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class YankiService implements YankiServiceImpl {

    @Autowired
    YankiRepository yankiRepository;

    @Override
    public Flux<YankiResponse> getAllYanki() {
        return yankiRepository.findAll().map(yanki -> YankiResponse.builder().amount(yanki.getAmount())
                .identityDocument(yanki.getIdentityDocument())
                .phoneNumber(yanki.getPhoneNumber())
                .build());
    }

    @Override
    public Flux<YankiResponse> getPhoneYanki(String phoneNumber) {
        return yankiRepository.findAll().filter(yanki -> yanki.getPhoneNumber().equals(phoneNumber))
                .map(yanki -> YankiResponse.builder().amount(yanki.getAmount())
                .identityDocument(yanki.getIdentityDocument())
                .phoneNumber(yanki.getPhoneNumber())
                .build());
    }

    @Override
    public Mono<Yanki> createYanki(Yanki yanki) {
        if (yanki.getIdentityDocument() == null || yanki.getPhoneNumber() == null ||
                yanki.getImeiNumber() == null || yanki.getEmail() == null) {
            return Mono.error(new IllegalArgumentException("Debe proporcionar documento de identidad, celular, IMEI y correo electrónico"));
        }

        if (!yanki.getPhoneNumber().matches("\\d{9}")) {
            return Mono.error(new IllegalArgumentException("El número de teléfono debe tener 9 dígitos"));
        }

        if (!yanki.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return Mono.error(new IllegalArgumentException("Correo electrónico inválido"));
        }

        if (yanki.getImeiNumber().length() < 15 || yanki.getImeiNumber().length() > 17) {
            return Mono.error(new IllegalArgumentException("El IMEI debe tener entre 15 y 17 caracteres"));
        }

        return yankiRepository.save(yanki);
    }

    @Override
    public Mono<Yanki> transfer(String senderPhone, String receiverPhone, Double amount) {
        if (amount == null || amount <= 0) {
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a cero"));
        }

        Mono<Yanki> senderMono = yankiRepository.findByPhoneNumber(senderPhone)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El remitente no existe")));

        Mono<Yanki> receiverMono = yankiRepository.findByPhoneNumber(receiverPhone)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El destinatario no existe")));

        return Mono.zip(senderMono, receiverMono)
                .flatMap(tuple -> {
                    Yanki sender = tuple.getT1();
                    Yanki receiver = tuple.getT2();

                    if (sender.getDebitCardNumber() != null) {
                        return Mono.error(new IllegalArgumentException("El saldo proviene de la cuenta bancaria, no del monedero"));
                    }

                    if (sender.getAmount() < amount) {
                        return Mono.error(new IllegalArgumentException("Saldo insuficiente"));
                    }

                    sender.setAmount(sender.getAmount() - amount);
                    receiver.setAmount(receiver.getAmount() + amount);

                    return Mono.when(yankiRepository.save(sender), yankiRepository.save(receiver))
                            .thenReturn(receiver);
                });
    }

    @Override
    public Mono<Yanki> linkDebitCard(String phoneNumber, String debitCardNumber, Long numberAccount) {
        return yankiRepository.findByPhoneNumber(phoneNumber)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Monedero no encontrado")))
                .flatMap(yanki -> {
                    if (yanki.getDebitCardNumber() != null) {
                        return Mono.error(new IllegalArgumentException("El monedero ya tiene una tarjeta asociada"));
                    }
                    yanki.setDebitCardNumber(debitCardNumber);
                    yanki.setNumberAccount(numberAccount);
                    return yankiRepository.save(yanki);
                });
    }

}