package com.bank.customer_service.service;

import com.bank.customer_service.dto.TransferRequest;
import com.bank.customer_service.exception.BadRequestException;
import com.bank.customer_service.exception.ResourceNotFoundException;
import com.bank.customer_service.model.Customer;
import com.bank.customer_service.model.enums.CustomerType;
import com.bank.customer_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final TransactionClient transactionClient;

    // Método para iniciar la transferencia
    public Mono<Boolean> initiateTransfer(TransferRequest transferRequest) {
        return findById(transferRequest.getFromCustomerId())
                .switchIfEmpty(Mono.error(new BadRequestException("Cliente origen no encontrado")))
                .flatMap(fromCustomer ->
                        findById(transferRequest.getToCustomerId())
                                .switchIfEmpty(Mono.error(new BadRequestException("Cliente destino no encontrado")))
                                .flatMap(toCustomer ->
                                        transactionClient.transferAmount(transferRequest)
                                                .onErrorResume(e -> Mono.error(new BadRequestException("Error al realizar la transferencia: " + e.getMessage())))
                                )
                );
    }

    public CustomerService(CustomerRepository customerRepository, TransactionClient transactionClient) {
        this.customerRepository = customerRepository;
        this.transactionClient = transactionClient;
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

        // Validación para clientes VIP
        if (customer.getCustomerType() == CustomerType.VIP && !customer.isHasCreditCard()) {
            return Mono.error(new BadRequestException("El cliente VIP debe tener una tarjeta de crédito."));
        }
        if (customer.getCustomerType() == CustomerType.VIP && customer.getRequiredAverageBalance() == null) {
            return Mono.error(new BadRequestException("El cliente VIP debe tener un saldo promedio requerido."));
        }

        // Validación para clientes PYME
        if (customer.getCustomerType() == CustomerType.PYME && !customer.isHasCreditCard()) {
            return Mono.error(new BadRequestException("El cliente PYME debe tener una tarjeta de crédito."));
        }

        // Asegúrate de que el repositorio no devuelva null
        return customerRepository.save(customer)
                .flatMap(savedCustomer -> {
                    log.info("Cliente guardado con éxito: {}", savedCustomer);
                    return Mono.just(savedCustomer);
                })
                .switchIfEmpty(Mono.error(new BadRequestException("No se pudo guardar el cliente.")))
                .doOnError(error -> log.error("Error guardando cliente: {}", error.getMessage()));
    }


    public Mono<Void> deleteById(String id) {
        return customerRepository.findById(id)
                .flatMap(existingCustomer -> {
                    log.info("Eliminando cliente con ID: {}", id);
                    return customerRepository.deleteById(id)
                            .doOnSuccess(aVoid -> log.info("Cliente con ID: {} eliminado correctamente.", id))
                            .doOnError(error -> log.error("Error eliminando cliente con ID: {}", id, error));
                })
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found with id: " + id)));
    }

    public BigDecimal calculateCommission(Customer customer, BigDecimal amount) {
        BigDecimal commission = BigDecimal.ZERO;

        // No comisiones para clientes VIP
        if (customer.getCustomerType() == CustomerType.VIP) {
            return commission;
        }

        // Comisiones para clientes PYME
        if (customer.getCustomerType() == CustomerType.PYME) {
            commission = amount.multiply(BigDecimal.valueOf(0.02)); // Ejemplo: 2% de comisión
        }

        return commission;
    }

}

