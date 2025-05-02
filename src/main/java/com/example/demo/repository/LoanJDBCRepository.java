package com.example.demo.repository;

import com.example.demo.model.Client;
import com.example.demo.model.Loan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@DependsOn("clientJDBCRepository")
public class LoanJDBCRepository {
    private final JdbcTemplate jdbcTemplate;

    public LoanJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS loans (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                client_id BIGINT NOT NULL,
                amount DOUBLE NOT NULL,
                interest_rate DOUBLE NOT NULL,
                term_months INT NOT NULL,
                start_date DATE NOT NULL,
                status VARCHAR(50) NOT NULL,
                FOREIGN KEY (client_id) REFERENCES clients(id)
            )
            """);
    }

    private final RowMapper<Loan> rowMapper = (rs, rowNum) -> {
        Loan loan = new Loan();
        loan.setId(rs.getLong("id"));
        loan.setClientId(rs.getLong("client_id"));
        loan.setAmount(rs.getDouble("amount"));
        loan.setInterestRate(rs.getDouble("interest_rate"));
        loan.setTermMonths(rs.getInt("term_months"));
        loan.setStartDate(rs.getDate("start_date").toLocalDate());
        loan.setStatus(rs.getString("status"));
        return loan;
    };

    public Loan save(Loan loan) {
        String sql = """
            INSERT INTO loans (client_id, amount, interest_rate, term_months, start_date, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
                loan.getClientId(),
                loan.getAmount(),
                loan.getInterestRate(),
                loan.getTermMonths(),
                loan.getStartDate(),
                loan.getStatus());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        loan.setId(id);
        return loan;
    }

    public List<Loan> findAll() {
        return jdbcTemplate.query("SELECT * FROM loans", rowMapper);
    }

    public Optional<Loan> findById(Long id) {
        try {
            Loan loan = jdbcTemplate.queryForObject(
                    "SELECT * FROM loans WHERE id = ?",
                    rowMapper, id);
            return Optional.ofNullable(loan);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Loan> findByClientId(Long clientId) {
        return jdbcTemplate.query(
                "SELECT * FROM loans WHERE client_id = ?",
                rowMapper, clientId);
    }
}