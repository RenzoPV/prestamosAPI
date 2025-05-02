package com.example.demo.controller;

import com.example.demo.dto.IdRequest;
import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Validated
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request) {
        LoanResponse response = loanService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoan(@Valid IdRequest request) {
        LoanResponse response = loanService.getLoanDetails(request.id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Loan>> getLoansByClient(@PathVariable Long clientId) {
        IdRequest request = new IdRequest(clientId);
        List<Loan> loans = loanService.getLoansByClientId(request.id());
        return ResponseEntity.ok(loans);
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }
}