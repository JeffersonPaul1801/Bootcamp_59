package com.bank.transactions_service.service;

import com.bank.transactions_service.exception.ResourceNotFoundException;
import com.bank.transactions_service.model.Transaction;
import com.bank.transactions_service.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Flux<Transaction> findAll() {
        log.info("Obteniendo todas las transacciones");
        return transactionRepository.findAll()
                .doOnComplete(() -> log.info("Consulta de transacciones completada"));
    }

    public Mono<Transaction> findById(String id) {
        log.info("Buscando transacción con ID: {}", id);
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Transaction not found with id: " + id)))
                .doOnSuccess(transaction -> log.debug("Transacción encontrada: {}", transaction));
    }

    public Flux<Transaction> findByCustomerId(String customerId) {
        log.info("Buscando transacciones del cliente con ID: {}", customerId);
        return transactionRepository.findByCustomerId(customerId)
                .doOnComplete(() -> log.info("Búsqueda de transacciones por cliente completada"));
    }

    public Flux<Transaction> findByAccountId(String accountId) {
        log.info("Buscando transacciones de la cuenta con ID: {}", accountId);
        return transactionRepository.findByAccountId(accountId)
                .doOnComplete(() -> log.info("Búsqueda de transacciones por cuenta completada"));
    }

    public Mono<Transaction> save(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().doubleValue() <= 0) {
            log.warn("Monto de transacción inválido: {}", transaction.getAmount());
            return Mono.error(new IllegalArgumentException("Transaction amount must be greater than zero"));
        }
        transaction.setTimestamp(LocalDateTime.now());
        log.info("Guardando nueva transacción: {}", transaction);
        return transactionRepository.save(transaction)
                .doOnSuccess(savedTransaction -> log.info("Transacción guardada con éxito: {}", savedTransaction))
                .doOnError(error -> log.error("Error al guardar la transacción: {}", error.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        log.info("Eliminando transacción con ID: {}", id);
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Transaction not found with id: " + id)))
                .flatMap(existingTransaction -> {
                    log.warn("Transacción eliminada: {}", existingTransaction);
                    return transactionRepository.deleteById(id);
                });
    }
}

