package com.bank.customer_service.service;

import com.bank.customer_service.dto.TransferRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TransactionClient {

    private final WebClient webClient;

    // Constructor para inyectar WebClient
    public TransactionClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://transactions-service").build();  // URL de transactions-service
    }

    // Método para realizar la transferencia
    public Mono<Boolean> transferAmount(TransferRequest transferRequest) {
        return this.webClient.post()
                .uri("/api/transactions/transfer")  // Ajusta la URI según el endpoint real de tu transactions-service
                .bodyValue(transferRequest)
                .retrieve()
                .bodyToMono(Boolean.class);  // Espera una respuesta booleana
    }
}



