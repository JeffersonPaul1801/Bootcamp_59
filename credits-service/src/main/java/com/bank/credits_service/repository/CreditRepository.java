package com.bank.credits_service.repository;

import com.bank.credits_service.model.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
    Flux<Credit> findByCustomerId(String customerId);
}

