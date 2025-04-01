package com.bank.customer_service.repository;

import com.bank.customer_service.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findByDocumentNumber(String documentNumber);
}

