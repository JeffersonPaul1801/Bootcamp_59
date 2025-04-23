package com.microservice.controller;

import com.microservice.exception.BusinessException;
import com.microservice.model.DebitCardRequest;
import com.microservice.model.PaymentRequest;
import com.microservice.service.DebitCardService;
import com.microservice.service.client.CustomerClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/debit")
public class DebitController {

    private final DebitCardService debitCardService;

    @PostMapping("/pay")
    public Mono<ResponseEntity<String>> payWithDebitCard(@RequestBody PaymentRequest paymentRequest) {
        return debitCardService.payWithDebitCard(paymentRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @PostMapping("/{cardId}/associate-all-accounts")
    public Mono<ResponseEntity<String>> associateAllAccounts(@PathVariable String cardId) {
        return debitCardService.associateAllAccountsToCard(cardId)
                .then(Mono.just(ResponseEntity.ok("Tarjeta asociada a todas las cuentas del cliente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @PutMapping("/{cardId}/set-primary-account/{accountId}")
    public Mono<ResponseEntity<String>> setPrimaryAccount(@PathVariable String cardId, @PathVariable String accountId) {
        return debitCardService.setPrimaryAccount(cardId, accountId)
                .then(Mono.just(ResponseEntity.ok("Cuenta principal asignada correctamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @PostMapping("/{debitCardId}/transactions")
    public Mono<ResponseEntity<String>> makePayment(@PathVariable String debitCardId, @RequestBody PaymentRequest request) {
        return debitCardService.processDebitTransaction(debitCardId, request.getAmount())
                .then(Mono.just(ResponseEntity.ok("Pago realizado con éxito.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @GetMapping("/{cardNumber}/balance")
    public Mono<ResponseEntity<BigDecimal>> getPrimaryAccountBalance(@PathVariable String cardNumber) {
        return debitCardService.getPrimaryAccountBalance(cardNumber)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(BigDecimal.ZERO)));
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<String>> createDebitCard(@RequestBody DebitCardRequest request) {
        return debitCardService.createDebitCard(request)
                .map(id -> ResponseEntity.ok("Tarjeta de débito creada con ID: " + id))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
}
