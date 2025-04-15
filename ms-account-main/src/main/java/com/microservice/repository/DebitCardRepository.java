package com.microservice.repository;

import com.microservice.model.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {
    Mono<DebitCard> findByCardNumber(String cardNumber);
}
