package com.example.demo.service;

import com.example.demo.repository.ClientJDBCRepository;
import org.springframework.stereotype.Service;
import com.example.demo.model.Client;
import com.example.demo.dto.ClientRequest;
import com.example.demo.exception.ValidationException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientJDBCRepository clientRepository;

    public ClientService(ClientJDBCRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(ClientRequest request) {
        // Verificar si el cliente ya existe
        clientRepository.findByDni(request.getDni()).ifPresent(c -> {
            throw new BusinessException("El cliente con DNI " + request.getDni() + " ya existe");
        });

        Client client = new Client();
        client.setDni(request.getDni());
        client.setName(request.getName());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());
        return clientRepository.save(client);
    }

    public Client findByDni(String dni) {
        return clientRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con DNI " + dni + " no encontrado"));
    }
}
