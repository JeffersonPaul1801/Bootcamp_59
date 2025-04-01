package com.bank.accounts_service.service;

import com.bank.accounts_service.exception.BadRequestException;
import com.bank.accounts_service.exception.ResourceNotFoundException;
import com.bank.accounts_service.model.BankAccount;
import com.bank.accounts_service.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import java.math.BigDecimal;

@Slf4j
@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
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
}

