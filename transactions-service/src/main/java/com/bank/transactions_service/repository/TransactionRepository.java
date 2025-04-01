package com.bank.transactions_service.repository;

import com.bank.transactions_service.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByCustomerId(String customerId);
    Flux<Transaction> findByAccountId(String accountId);
}

