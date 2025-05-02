package com.example.demo.controller;

import com.example.demo.dto.ClientDniRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Client;
import com.example.demo.dto.ClientRequest;
import com.example.demo.service.ClientService;

@RestController
@RequestMapping("/api/clients")
@Validated
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody ClientRequest request) {
        Client client = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Client> getClient(@Valid ClientDniRequest request) {
        Client client = clientService.findByDni(request.dni());
        return ResponseEntity.ok(client);
    }
}