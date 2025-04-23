package com.microservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "yanki_request", groupId = "bootcamp")
    public void consume(String message) {
        System.out.println("Mensaje recibido: " + message);
    }
}
