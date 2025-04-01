package com.bank.accounts_service.controller;

import com.bank.accounts_service.model.BankAccount;
import com.bank.accounts_service.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(summary = "Obtener todas las cuentas bancarias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuentas encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccount.class)))
    })
    @GetMapping
    public Flux<BankAccount> getAllAccounts() {
        return bankAccountService.findAll();
    }

    @Operation(summary = "Obtener una cuenta bancaria por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccount.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> getAccountById(@PathVariable String id) {
        return bankAccountService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener cuentas bancarias por ID de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuentas encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccount.class)))
    })
    @GetMapping("/customer/{customerId}")
    public Flux<BankAccount> getAccountsByCustomer(@PathVariable String customerId) {
        return bankAccountService.findByCustomerId(customerId);
    }

    @Operation(summary = "Crear una nueva cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccount.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<BankAccount>> createAccount(@Valid @RequestBody BankAccount bankAccount) {
        return bankAccountService.save(bankAccount)
                .map(savedAccount -> ResponseEntity.status(HttpStatus.CREATED).body(savedAccount));
    }

    @Operation(summary = "Eliminar una cuenta bancaria por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable String id) {
        return bankAccountService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}

