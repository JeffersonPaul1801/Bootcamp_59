package com.bank.customer_service.controller;


import com.bank.customer_service.model.Customer;
import com.bank.customer_service.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Obtener todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)))
    })
    @GetMapping
    public Flux<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @Operation(summary = "Obtener cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable String id) {
        return customerService.findById(id);
    }

    @Operation(summary = "Obtener cliente por número de documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)))
    })
    @GetMapping("/document/{documentNumber}")
    public Mono<Customer> getCustomerByDocument(@PathVariable String documentNumber) {
        return customerService.findByDocumentNumber(documentNumber);
    }

    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)))
    })
    @PostMapping
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @Operation(summary = "Eliminar cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable String id) {
        return customerService.deleteById(id);
    }
}

