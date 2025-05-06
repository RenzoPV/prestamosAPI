package com.example.demo.controller;

import com.example.demo.dto.ClientRequest;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;
    private ClientService clientService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        clientService = Mockito.mock(ClientService.class);

        ClientController clientController = new ClientController(clientService);

        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCreateClient() throws Exception {
        ClientRequest request = new ClientRequest();
        request.setDni("12345678");
        request.setName("Juan");
        request.setPhone("999888777");
        request.setEmail("juan@gmail.com");

        Client expectedClient = new Client();
        expectedClient.setId(1L);
        expectedClient.setDni("12345678");
        expectedClient.setName("Juan");
        expectedClient.setPhone("999888777");
        expectedClient.setEmail("juan@gmail.com");

        when(clientService.createClient(any(ClientRequest.class))).thenReturn(expectedClient);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.phone").value("999888777"))
                .andExpect(jsonPath("$.email").value("juan@gmail.com"));
    }

    @Test
    void testGetClient() throws Exception {
        String dni = "12345678";

        Client expectedClient = new Client();
        expectedClient.setId(1L);
        expectedClient.setDni(dni);
        expectedClient.setName("María");
        expectedClient.setPhone("987654321");
        expectedClient.setEmail("maria@example.com");

        when(clientService.findByDni(dni)).thenReturn(expectedClient);

        mockMvc.perform(get("/api/clients/{dni}", dni))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dni").value(dni))
                .andExpect(jsonPath("$.name").value("María"))
                .andExpect(jsonPath("$.phone").value("987654321"))
                .andExpect(jsonPath("$.email").value("maria@example.com"));
    }

    @Test
    void testGetClientInvalidDni() throws Exception {
        String invalidDni = "123";

        mockMvc.perform(get("/api/clients/{dni}", invalidDni))
                .andExpect(status().isBadRequest());
                //.andExpect(jsonPath("$.message").value(containsString("El DNI debe tener exactamente 8 dígitos numéricos")));
    }
}