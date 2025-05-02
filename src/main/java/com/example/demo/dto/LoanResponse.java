package com.example.demo.dto;

import com.example.demo.model.Client;
import com.example.demo.model.Loan;
import lombok.Data;

//@Data
public class LoanResponse {
    private Client client;
    private Loan loan;
    private String status;
    private String message;

    public LoanResponse(Client client, Loan loan, String status, String message) {
        this.client = client;
        this.loan = loan;
        this.status = status;
        this.message = message;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}