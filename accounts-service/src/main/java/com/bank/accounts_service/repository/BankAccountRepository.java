package com.bank.accounts_service.repository;

import com.bank.accounts_service.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Flux<BankAccount> findByCustomerId(String customerId);
}

