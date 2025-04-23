package com.microservice.controller;

import com.microservice.producerKafka.KafkaProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class KafkaController {

    private KafkaProducer kafkaProducer;

    public void MessageController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/create")
    public String publish(@RequestParam String message) {
        kafkaProducer.publishMessage(message);
        return "Mensaje publicado en Kafka";
    }
}
