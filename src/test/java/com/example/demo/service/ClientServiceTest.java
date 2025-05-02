package com.example.demo.service;

import com.example.demo.dto.ClientRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientJDBCRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientJDBCRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void createClient_Success() {
        ClientRequest request = new ClientRequest();
        request.setDni("12345678");
        request.setName("Test Client");
        request.setPhone("985294666");
        request.setEmail("test@example.com");

        Client savedClient = new Client();
        savedClient.setId(1L);
        savedClient.setDni("12345678");
        savedClient.setName("Test Client");
        savedClient.setPhone("985294666");
        savedClient.setEmail("test@example.com");

        when(clientRepository.findByDni(any())).thenReturn(Optional.empty());
        when(clientRepository.save(any())).thenReturn(savedClient);

        Client result = clientService.createClient(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("12345678", result.getDni());
        assertEquals("Test Client", result.getName());
        assertEquals("985294666", result.getPhone());
        assertEquals("test@example.com", result.getEmail());
        verify(clientRepository, times(1)).findByDni("12345678");
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void createClient_ThrowsWhenDniExists() {
        ClientRequest request = new ClientRequest();
        request.setDni("12345678");

        Client existingClient = new Client();
        existingClient.setDni("12345678");

        when(clientRepository.findByDni("12345678")).thenReturn(Optional.of(existingClient));

        assertThrows(BusinessException.class, () -> clientService.createClient(request));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void findByDni_Success() {
        String dni = "12345678";
        Client client = new Client();
        client.setDni(dni);

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));

        Client result = clientService.findByDni(dni);

        assertNotNull(result);
        assertEquals(dni, result.getDni());
    }

    @Test
    void findByDni_ThrowsWhenNotFound() {
        String dni = "99999999";
        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.findByDni(dni));
    }
}