package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

//@Data
public class LoanRequest {
    // Client data
    @NotBlank(message = "DNI no puede estar vacío")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private String dni;

    // Loan data
    @NotNull(message = "Monto no puede estar vacío")
    @Positive(message = "Monto debe ser mayor que cero")
    private Double amount;

    @NotNull(message = "Tasa de interés no puede estar vacía")
    @PositiveOrZero(message = "Tasa de interés no puede ser negativa")
    private Double interestRate;

    @NotNull(message = "Plazo en meses no puede estar vacío")
    @Min(value = 1, message = "Plazo mínimo es 1 mes")
    @Max(value = 360, message = "Plazo máximo es 360 meses (30 años)")
    private Integer termMonths;

    @NotBlank(message = "Estado no puede estar vacío")
    private String status;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}