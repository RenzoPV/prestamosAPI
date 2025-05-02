package com.example.demo.service;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import com.example.demo.model.Client;
import com.example.demo.model.Loan;
import com.example.demo.repository.ClientJDBCRepository;
import com.example.demo.repository.LoanJDBCRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.exception.ValidationException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanJDBCRepository loanRepository;
    private final ClientJDBCRepository clientRepository;

    public LoanService(LoanJDBCRepository loanRepository, ClientJDBCRepository clientRepository) {
        this.loanRepository = loanRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public LoanResponse createLoan(LoanRequest request) {
        // Verifica que el cliente exista
        Client client = clientRepository.findByDni(request.getDni())
                .orElseThrow(() -> new BusinessException("Cliente no registrado"));

        Loan loan = new Loan();
        loan.setClientId(client.getId());
        loan.setAmount(request.getAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTermMonths(request.getTermMonths());
        loan.setStartDate(LocalDate.now());
        loan.setStatus(request.getStatus());

        Loan savedLoan = loanRepository.save(loan);
        return new LoanResponse(client, savedLoan, "SUCCESS", "Préstamo creado");
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByClientId(Long clientId) {
        List<Loan> loans = loanRepository.findByClientId(clientId);
        if (loans.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron préstamos para el cliente con ID " + clientId);
        }
        return loans;
    }

    public LoanResponse getLoanDetails(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo con ID " + id + " no encontrado"));

        //Validación opcional
        Client client = clientRepository.findById(loan.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado para el préstamo"));

        return new LoanResponse(client, loan, "SUCCESS", "Préstamo encontrado");
    }
}