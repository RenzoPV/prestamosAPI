package com.example.demo.dto;

import jakarta.validation.constraints.Pattern;

public record ClientDniRequest(
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos numéricos")
        String dni
) {}