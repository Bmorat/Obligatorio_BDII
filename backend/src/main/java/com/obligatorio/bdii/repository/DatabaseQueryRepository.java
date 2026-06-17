package com.obligatorio.bdii.repository;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseQueryRepository {

    // JdbcTemplate ejecuta el SQL usando la conexion configurada en application.properties.
    private final JdbcTemplate jdbcTemplate;

    public DatabaseQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
