package com.microservice.repository;

import com.microservice.model.YankiDeposit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YankiDepositRepository extends ReactiveMongoRepository<YankiDeposit, String> {
}
