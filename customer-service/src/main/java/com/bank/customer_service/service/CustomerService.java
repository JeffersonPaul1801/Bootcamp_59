package com.bank.customer_service.service;

import com.bank.customer_service.exception.ResourceNotFoundException;
import com.bank.customer_service.model.Customer;
import com.bank.customer_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Mono<Customer> findById(String id) {
        log.info("Buscando cliente con ID: {}", id);
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found with id: " + id)))
                .doOnSuccess(customer -> log.debug("Cliente encontrado: {}", customer));
    }

    public Mono<Customer> findByDocumentNumber(String documentNumber) {
        return customerRepository.findByDocumentNumber(documentNumber);
    }

    public Mono<Customer> save(Customer customer) {
        log.info("Guardando nuevo cliente: {}", customer);
        return customerRepository.save(customer)
                .doOnSuccess(savedCustomer -> log.info("Cliente guardado con éxito: {}", savedCustomer))
                .doOnError(error -> log.error("Error guardando cliente: {}", error.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        return customerRepository.deleteById(id);
    }
}

