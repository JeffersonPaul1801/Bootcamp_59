package com.bank.transactions_service.controller;

import com.bank.transactions_service.model.Transaction;
import com.bank.transactions_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Obtener todas las transacciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public Flux<Transaction> getAllTransactions() {
        return transactionService.findAll();
    }

    @Operation(summary = "Obtener una transacción por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public Mono<Transaction> getTransactionById(@PathVariable String id) {
        return transactionService.findById(id);
    }

    @Operation(summary = "Obtener transacciones por ID de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/customer/{customerId}")
    public Flux<Transaction> getTransactionsByCustomer(@PathVariable String customerId) {
        return transactionService.findByCustomerId(customerId);
    }

    @Operation(summary = "Obtener transacciones por ID de cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/account/{accountId}")
    public Flux<Transaction> getTransactionsByAccount(@PathVariable String accountId) {
        return transactionService.findByAccountId(accountId);
    }

    @Operation(summary = "Crear una nueva transacción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public Mono<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
    }

    @Operation(summary = "Eliminar una transacción por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transacción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTransaction(@PathVariable String id) {
        return transactionService.deleteById(id);
    }
}

