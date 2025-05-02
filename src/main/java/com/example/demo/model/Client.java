package com.example.demo.model;

import lombok.Data;

//@Data
public class Client {
    private Long id;
    private String dni;
    private String name;
    private String phone;
    private String email;

    public Client() {}

    public Client(Long id, String dni, String name, String email, String phone) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}