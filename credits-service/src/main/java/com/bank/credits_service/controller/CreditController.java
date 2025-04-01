package com.bank.credits_service.controller;

import com.bank.credits_service.model.Credit;
import com.bank.credits_service.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/credits")
public class CreditController {
    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @Operation(summary = "Obtener todos los créditos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Créditos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credit.class)))
    })
    @GetMapping
    public Flux<Credit> getAllCredits() {
        return creditService.findAll();
    }

    @Operation(summary = "Obtener crédito por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credit.class))),
            @ApiResponse(responseCode = "404", description = "Crédito no encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public Mono<Credit> getCreditById(@PathVariable String id) {
        return creditService.findById(id);
    }

    @Operation(summary = "Obtener créditos por cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Créditos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credit.class)))
    })
    @GetMapping("/customer/{customerId}")
    public Flux<Credit> getCreditsByCustomer(@PathVariable String customerId) {
        return creditService.findByCustomerId(customerId);
    }

    @Operation(summary = "Crear un nuevo crédito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Crédito creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credit.class)))
    })
    @PostMapping
    public Mono<Credit> createCredit(@RequestBody Credit credit) {
        return creditService.save(credit);
    }

    @Operation(summary = "Eliminar crédito por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Crédito eliminado",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Crédito no encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCredit(@PathVariable String id) {
        return creditService.deleteById(id);
    }
}

