package br.com.transaction.controller;

import br.com.transaction.model.ExchangeDTO;
import br.com.transaction.model.TransactionRequest;
import br.com.transaction.model.TransactionResponse;
import br.com.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("v1")
public class Controller {
    @Autowired
    TransactionService service;

    public Controller( TransactionService service){
        this.service = service;
    }

    @PostMapping("createTransaction")
    ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transaction){
        var transaction1 = service.create(transaction);
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(transaction1.getId()).toUri();
        return ResponseEntity.created(location).body(transaction1);

    }

    @GetMapping("{transactionIdentifier}/{countryCurrency}")
    public ResponseEntity<ExchangeDTO> retrieveExchaged(@PathVariable Long transactionIdentifier, @PathVariable String countryCurrency){
        var exchange = service.retrieve(transactionIdentifier,countryCurrency);
        return ResponseEntity.ok(exchange);
    }

}
