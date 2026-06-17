package com.obligatorio.bdii.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseStatusRepository {

    // Este repository concentra las consultas tecnicas sobre el estado de la BD.
    private final JdbcTemplate jdbcTemplate;

    public DatabaseStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getCurrentDatabaseName() {
        return jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
    }

    public Integer countCurrentDatabaseTables() {
        return jdbcTemplate.queryForObject("""
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = DATABASE()
            """, Integer.class);
    }
}
