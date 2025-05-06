package com.example.demo.service;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.model.Loan;
import com.example.demo.repository.ClientJDBCRepository;
import com.example.demo.repository.LoanJDBCRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanJDBCRepository loanRepository;

    @Mock
    private ClientJDBCRepository clientRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void createLoan_Success() {
        LoanRequest request = new LoanRequest();
        request.setDni("12345678");
        request.setAmount(1000.0);
        request.setInterestRate(5.0);
        request.setTermMonths(12);
        request.setStatus("activo");

        Client client = new Client();
        client.setId(1L);
        client.setDni("12345678");

        Loan savedLoan = new Loan();
        savedLoan.setId(1L);
        savedLoan.setClientId(1L);
        savedLoan.setAmount(1000.0);
        savedLoan.setTermMonths(12);
        savedLoan.setInterestRate(3.5);
        savedLoan.setStatus("activo");
        savedLoan.setStartDate(LocalDate.now());

        when(clientRepository.findByDni("12345678")).thenReturn(Optional.of(client));
        when(loanRepository.save(any())).thenReturn(savedLoan);

        LoanResponse response = loanService.createLoan(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Préstamo creado", response.getMessage());
        assertEquals(client, response.getClient());
        assertEquals(savedLoan, response.getLoan());
        verify(loanRepository, times(1)).save(any());
        verify(clientRepository, times(1)).findByDni("12345678");
    }

    @Test
    void createLoan_ThrowsWhenClientNotFound() {
        LoanRequest request = new LoanRequest();
        request.setDni("99999999");

        when(clientRepository.findByDni("99999999")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> loanService.createLoan(request));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void getLoanDetails_Success() {
        Long loanId = 1L;
        Loan loan = new Loan();
        loan.setId(loanId);
        loan.setClientId(1L);
        loan.setAmount(2000.0);
        loan.setTermMonths(12);
        loan.setInterestRate(3.5);
        loan.setStatus("activo");
        loan.setStartDate(LocalDate.now());

        Client client = new Client();
        client.setId(1L);
        client.setEmail("cliente@gmail.com");
        client.setName("cliente test");
        client.setPhone("666555444");
        client.setDni("12345678");

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        LoanResponse response = loanService.getLoanDetails(loanId);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Préstamo encontrado", response.getMessage());
        assertEquals(client, response.getClient());
        assertEquals(loan, response.getLoan());
    }

    @Test
    void getLoanDetails_ThrowsWhenLoanNotFound() {
        Long loanId = 999L;
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoanDetails(loanId));
    }

    @Test
    void getLoanDetails_ThrowsWhenClientNotFound() {
        Long loanId = 1L;
        Loan loan = new Loan();
        loan.setId(loanId);
        loan.setClientId(1L);
        loan.setAmount(2000.0);
        loan.setTermMonths(12);
        loan.setInterestRate(3.5);
        loan.setStatus("activo");
        loan.setStartDate(LocalDate.now());

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(clientRepository.findById(loan.getClientId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoanDetails(loanId));
    }

    @Test //pendiente
    void getLoansByClientId_Success() {
        Long clientId = 1L;
        Loan loan1 = new Loan();
        loan1.setClientId(clientId);
        Loan loan2 = new Loan();
        loan2.setClientId(clientId);

        when(loanRepository.findByClientId(clientId)).thenReturn(List.of(loan1, loan2));

        List<Loan> loans = loanService.getLoansByClientId(clientId);

        assertEquals(2, loans.size());
        assertEquals(loan1, loans.get(0));
        assertEquals(loan2, loans.get(1));
    }

    @Test
    void getLoansByClientId_ThrowsWhenNoLoansFound() {
        Long clientId = 999L;
        when(loanRepository.findByClientId(clientId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoansByClientId(clientId));
    }

    @Test
    void getAllLoans_Success() {
        Long clientId = 1L;

        Loan loan1 = new Loan();
        loan1.setId(1L);
        loan1.setClientId(clientId);
        loan1.setAmount(8000.0);
        loan1.setInterestRate(5.5);
        loan1.setTermMonths(6);
        loan1.setStartDate(LocalDate.now());
        loan1.setStatus("activo");

        Loan loan2 = new Loan();
        loan2.setId(2L);
        loan2.setClientId(clientId);
        loan2.setAmount(15000.0);
        loan2.setInterestRate(10.3);
        loan2.setTermMonths(12);
        loan2.setStartDate(LocalDate.now());
        loan2.setStatus("activo");

        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));

        List<Loan> loans = loanService.getAllLoans();
        assertEquals(2, loans.size());
        assertEquals(loan1, loans.get(0));
        assertEquals(loan2, loans.get(1));
    }
}