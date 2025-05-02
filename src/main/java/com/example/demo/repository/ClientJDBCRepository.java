package com.example.demo.repository;

import com.example.demo.model.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientJDBCRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClientJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS clients (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                dni VARCHAR(20) NOT NULL UNIQUE,
                name VARCHAR(100) NOT NULL,
                phone VARCHAR(20),
                email VARCHAR(100)
            )
            """);
    }

    private final RowMapper<Client> rowMapper = (rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setDni(rs.getString("dni"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        return client;
    };

    public Client save(Client client) {
        String sql = "INSERT INTO clients (dni, name, phone, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, client.getDni(), client.getName(), client.getPhone(), client.getEmail());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        client.setId(id);
        return client;
    }

    public Optional<Client> findByDni(String dni) {
        try {
            Client client = jdbcTemplate.queryForObject(
                    "SELECT * FROM clients WHERE dni = ?",
                    rowMapper, dni);
            return Optional.ofNullable(client);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Client> findById(Long id) {
        try {
            Client client = jdbcTemplate.queryForObject(
                    "SELECT * FROM clients WHERE id = ?",
                    rowMapper, id);
            return Optional.ofNullable(client);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
