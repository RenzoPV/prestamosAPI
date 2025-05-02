package com.example.demo.dto;

import jakarta.validation.constraints.Min;

public record IdRequest(
        @Min(value = 1, message = "El ID debe ser un número positivo mayor a 0")
        Long id
) {}
