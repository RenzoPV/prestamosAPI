package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

//@Data
public class ClientRequest {
    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    private String dni;

    @NotBlank(message = "Nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "Nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "Teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{9,15}$", message = "Teléfono debe contener solo números y tener entre 9 y 15 dígitos")
    private String phone;

    @NotBlank(message = "Email no puede estar vacío")
    @Email(message = "Email debe tener un formato válido")
    private String email;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}