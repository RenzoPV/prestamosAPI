package com.example.demo.model;

import lombok.Data;
import java.time.LocalDate;

//@Data
public class Loan {
    private Long id;
    private Long clientId;
    private Double amount;
    private Double interestRate;
    private Integer termMonths;
    private LocalDate startDate;
    private String status;

    public Loan() {
    }

    public Loan(Double amount, Long id, Long clientId, Double interestRate, Integer termMonths, LocalDate startDate, String status) {
        this.amount = amount;
        this.id = id;
        this.clientId = clientId;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.startDate = startDate;
        this.status = status;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }
}