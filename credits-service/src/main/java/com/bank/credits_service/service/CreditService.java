package com.bank.credits_service.service;

import com.bank.credits_service.exception.ResourceNotFoundException;
import com.bank.credits_service.model.Credit;
import com.bank.credits_service.repository.CreditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class CreditService {
    private static final Logger log = LoggerFactory.getLogger(CreditService.class);
    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public Flux<Credit> findAll() {
        log.info("Obteniendo todos los créditos");
        return creditRepository.findAll()
                .doOnComplete(() -> log.info("Consulta de créditos completada"));
    }

    public Mono<Credit> findById(String id) {
        log.info("Buscando crédito con ID: {}", id);
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Credits not found with id: " + id)))
                .doOnSuccess(credit -> log.debug("Crédito encontrado: {}", credit));
    }

    public Flux<Credit> findByCustomerId(String customerId) {
        log.info("Buscando créditos del cliente con ID: {}", customerId);
        return creditRepository.findByCustomerId(customerId)
                .doOnComplete(() -> log.info("Búsqueda de créditos por cliente completada"));
    }

    public Mono<Credit> save(Credit credit) {
        log.info("Guardando nuevo crédito: {}", credit);
        return creditRepository.save(credit)
                .doOnSuccess(savedCredit -> log.info("Crédito guardado con éxito: {}", savedCredit))
                .doOnError(error -> log.error("Error al guardar el crédito: {}", error.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        log.info("Eliminando crédito con ID: {}", id);
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Credit not found with id: " + id)))
                .flatMap(existingCredit -> {
                    log.warn("Crédito eliminado: {}", existingCredit);
                    return creditRepository.deleteById(id);
                });
    }
}

