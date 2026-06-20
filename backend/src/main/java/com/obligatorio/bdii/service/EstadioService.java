package com.obligatorio.bdii.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.model.Estadio;

import java.util.List;

@Service
public class EstadioService {

    private final JdbcTemplate jdbcTemplate;

    public EstadioService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Estadio> obtenerEstadios() {
        String sql = "SELECT Id, Nombre, Ubicacion FROM Estadio";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Estadio e = new Estadio();
            e.setId(rs.getInt("Id"));
            e.setNombre(rs.getString("Nombre"));
            e.setUbicacion(rs.getString("Ubicacion"));
            return e;
        });
    }

    public int insertarEstadio(String nombre, String ubicacion) {
        String sql = "INSERT INTO Estadio (Nombre, Ubicacion) VALUES (?, ?)";
        return jdbcTemplate.update(sql, nombre, ubicacion);
    }
}