package com.bank.accounts_service.service;

import com.bank.accounts_service.dto.Customer;
import com.bank.accounts_service.exception.BadRequestException;
import com.bank.accounts_service.exception.ResourceNotFoundException;
import com.bank.accounts_service.model.BankAccount;
import com.bank.accounts_service.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankAccountService {

    private final WebClient webClient;
    private final BankAccountRepository bankAccountRepository;


    public Mono<BankAccount> transferFunds(String fromAccountId, String toAccountId, BigDecimal amount) {
        return bankAccountRepository.findById(fromAccountId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta de origen no encontrada con ID: " + fromAccountId)))
                .flatMap(fromAccount -> {
                    if (fromAccount.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new BadRequestException("Saldo insuficiente en la cuenta de origen"));
                    }

                    return calculateCommission(fromAccount, amount)
                            .flatMap(commission -> {
                                fromAccount.setBalance(fromAccount.getBalance().subtract(amount).subtract(commission));

                                return bankAccountRepository.findById(toAccountId)
                                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta de destino no encontrada con ID: " + toAccountId)))
                                        .flatMap(toAccount -> {
                                            toAccount.setBalance(toAccount.getBalance().add(amount));

                                            return Mono.zip(
                                                    bankAccountRepository.save(fromAccount),
                                                    bankAccountRepository.save(toAccount)
                                            ).map(savedAccounts -> {
                                                log.info("Transferencia completada: de {} a {}, monto: {}", fromAccountId, toAccountId, amount);
                                                return fromAccount;
                                            });
                                        });
                            });
                });
    }



    private Mono<BigDecimal> calculateCommission(BankAccount account, BigDecimal amount) {
        return webClient.get()
                .uri("/customers/{id}", account.getCustomerId())
                .retrieve()
                .bodyToMono(Customer.class)
                .flatMap(customer -> {
                    if ("VIP".equals(customer.getProfile())) {
                        return Mono.just(BigDecimal.ZERO);
                    }
                    return Mono.just(amount.multiply(BigDecimal.valueOf(0.01)));
                });
    }

    public Flux<BankAccount> findAll() {
        log.info("Obteniendo todas las cuentas bancarias");
        return bankAccountRepository.findAll()
                .doOnComplete(() -> log.info("Consulta de cuentas bancarias completada"));
    }

    public Mono<BankAccount> findById(String id) {
        log.info("Buscando cuenta bancaria con ID: {}", id);
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Account not found with id: " + id)))
                .doOnSuccess(account -> log.debug("Cuenta encontrada: {}", account));
    }

    public Flux<BankAccount> findByCustomerId(String customerId) {
        log.info("Buscando cuentas del cliente con ID: {}", customerId);
        return bankAccountRepository.findByCustomerId(customerId)
                .doOnComplete(() -> log.info("Búsqueda de cuentas por cliente completada"));
    }

    public Mono<BankAccount> save(@Valid BankAccount bankAccount) {
        log.info("Guardando nueva cuenta bancaria: {}", bankAccount);

        if ("CHECKING".equals(bankAccount.getAccountType())) {
            return webClient.get()
                    .uri("/customers/{id}", bankAccount.getCustomerId())
                    .retrieve()
                    .bodyToMono(Customer.class)
                    .flatMap(customer -> {
                        if ("PYME".equals(customer.getProfile())) {
                            bankAccount.setHasMaintenanceFee(false);
                        }
                        return bankAccountRepository.save(bankAccount)
                                .doOnSuccess(savedAccount -> log.info("Cuenta guardada con éxito: {}", savedAccount))
                                .doOnError(error -> log.error("Error al guardar la cuenta: {}", error.getMessage()));
                    });
        }

        // Guardar directamente si no es una cuenta CHECKING
        return bankAccountRepository.save(bankAccount)
                .doOnSuccess(savedAccount -> log.info("Cuenta guardada con éxito: {}", savedAccount))
                .doOnError(error -> log.error("Error al guardar la cuenta: {}", error.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        log.info("Eliminando cuenta bancaria con ID: {}", id);
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Account not found with id: " + id)))
                .flatMap(existingAccount -> {
                    log.warn("Cuenta eliminada: {}", existingAccount);
                    return bankAccountRepository.deleteById(id);
                });
    }

    // DTO para Customer
    private static class Customer {
        private String id;
        private String profile;

        public String getProfile() {
            return profile;
        }
    }
}

