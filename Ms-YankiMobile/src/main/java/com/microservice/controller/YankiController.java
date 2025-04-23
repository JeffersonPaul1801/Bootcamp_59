package com.microservice.controller;

import com.microservice.Dto.YankiDepositResponse;
import com.microservice.Dto.YankiResponse;
import com.microservice.model.Yanki;
import com.microservice.model.YankiDeposit;
import com.microservice.producerKafka.KafkaProducer;
import com.microservice.service.YankiDepositService;
import com.microservice.service.YankiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yanki")
@Slf4j
public class YankiController {

    @Autowired
    YankiService yankiService;

    @Autowired
    YankiDepositService depositService;

    @Autowired
    private KafkaProducer kafkaProducer;

    //Payment Yanki

    @GetMapping(value = "/allYanki")
    @ResponseStatus(HttpStatus.OK)
    public Flux<YankiResponse> getAllYanki(){
        log.info("Listar todos los monederos movil.");
        return yankiService.getAllYanki();
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Yanki> createYanki(@RequestBody Yanki yanki){
        log.info("Monedero movil creado con Exito.");
        return yankiService.createYanki(yanki);
    }

    @GetMapping(value = "/{phoneNumber}")
    public Flux<YankiResponse> getPhoneYanki(@PathVariable String phoneNumber){
        log.info("Listar monedero movil por numero celular");
        return yankiService.getPhoneYanki(phoneNumber);
    }

    //Deposit Yanki

    @GetMapping(value = "/deposit/allYankiDeposit")
    @ResponseStatus(HttpStatus.OK)
    public Flux<YankiDepositResponse> getAllYankiDeposit(){
        log.info("Listar todos los monederos movil.");
        return depositService.getAllYanki();
    }

    @PostMapping(value = "/deposit/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<YankiDeposit> createYankiDeposit(@RequestBody YankiDeposit yankiDeposit){
        log.info("Monedero movil creado con Exito.");
        return depositService.createYanki(yankiDeposit);
    }

    @GetMapping(value = "/deposit/{phoneNumber}")
    public Flux<YankiDepositResponse> getPhoneYankiDeposit(@PathVariable String phoneNumber){
        log.info("Listar monedero movil por numero celular");
        return depositService.getPhoneYanki(phoneNumber);
    }

    @PostMapping(value = "/account/yanki")
    public ResponseEntity<String> requestYanki (@RequestBody String yanki){
        kafkaProducer.publishMessage(yanki);
        return ResponseEntity.ok(yanki);
    }

    @PostMapping(value = "/account/deposit/yanki")
    public ResponseEntity<String> requestYankiDeposit (@RequestBody String yanki){
        kafkaProducer.publishMessage(yanki);
        return ResponseEntity.ok(yanki);
    }

    // tranferer Yanki

    @PutMapping("/transfer")
    public Mono<ResponseEntity<String>> transferMoney(@RequestParam String senderPhone,
                                                      @RequestParam String receiverPhone,
                                                      @RequestParam Double amount) {
        log.info("Procesando transferencia de {} desde {} a {}", amount, senderPhone, receiverPhone);
        return yankiService.transfer(senderPhone, receiverPhone, amount)
                .map(result -> ResponseEntity.ok("Transferencia exitosa"))
                .onErrorResume(e -> {
                    log.error("Error en transferencia: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    //associate debit card

    @PutMapping(value = "/linkDebitCard")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Yanki> linkDebitCard(@RequestParam String phoneNumber,
                                     @RequestParam String debitCardNumber,
                                     @RequestParam Long numberAccount) {
        log.info("Asociando tarjeta de débito al monedero.");
        return yankiService.linkDebitCard(phoneNumber, debitCardNumber, numberAccount);
    }

}
