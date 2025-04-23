package com.microservice.repository;

import com.microservice.model.Yanki;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface YankiRepository extends ReactiveMongoRepository<Yanki, String> {

    Mono<Yanki> findByPhoneNumber(String phoneNumber);
}
