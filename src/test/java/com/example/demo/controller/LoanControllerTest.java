package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.model.Client;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Arrays;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoanControllerTest {

    private MockMvc mockMvc;
    private LoanService loanService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        loanService = Mockito.mock(LoanService.class);
        LoanController loanController = new LoanController(loanService);
        mockMvc = MockMvcBuilders.standaloneSetup(loanController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createLoan_ShouldReturnCreatedLoan() throws Exception {
        LoanRequest request = new LoanRequest();
        request.setDni("12345678");
        request.setAmount(10000.0);
        request.setInterestRate(5.0);
        request.setTermMonths(12);
        request.setStatus("activo");

        Client client = new Client();
        client.setDni("12345678");

        Loan loan = new Loan();
        loan.setAmount(10000.0);
        loan.setInterestRate(5.0);
        loan.setTermMonths(12);
        loan.setStartDate(LocalDate.now());

        LoanResponse response = new LoanResponse(client, loan, "CREATED", "Loan created successfully");

        when(loanService.createLoan(any(LoanRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.client.dni").value("12345678"))
                .andExpect(jsonPath("$.loan.amount").value(10000.0))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void getLoan_ShouldReturnLoanDetails() throws Exception {
        Client client = new Client();
        client.setDni("12345678");

        Loan loan = new Loan();
        loan.setAmount(5000.0);
        loan.setTermMonths(6);

        LoanResponse response = new LoanResponse(client, loan, "ACTIVE", null);

        when(loanService.getLoanDetails(any(Long.class))).thenReturn(response);

        mockMvc.perform(get("/api/loans/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.dni").value("12345678"))
                .andExpect(jsonPath("$.loan.amount").value(5000.0));
    }

    @Test
    void getLoansByClient_ShouldReturnClientLoans() throws Exception {
        Loan loan1 = new Loan();
        loan1.setAmount(5000.0);
        loan1.setClientId(1L);

        Loan loan2 = new Loan();
        loan2.setAmount(10000.0);
        loan2.setClientId(1L);

        when(loanService.getLoansByClientId(1L)).thenReturn(Arrays.asList(loan1, loan2));

        mockMvc.perform(get("/api/loans/client/{clientId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(5000.0))
                .andExpect(jsonPath("$[1].amount").value(10000.0));
    }

    @Test
    void getAllLoans_ShouldReturnAllLoans() throws Exception {
        Loan loan1 = new Loan();
        loan1.setAmount(5000.0);
        loan1.setClientId(1L);

        Loan loan2 = new Loan();
        loan2.setAmount(10000.0);
        loan2.setClientId(2L);

        when(loanService.getAllLoans()).thenReturn(Arrays.asList(loan1, loan2));

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[1].clientId").value(2));
    }

    @Test
    void getLoan_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/loans/{id}", 0))
                .andExpect(status().isBadRequest());
    }
}