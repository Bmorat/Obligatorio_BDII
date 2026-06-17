package com.obligatorio.bdii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.model.Estadio;

import java.util.List;

@Service
public class EstadioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Estadio> obtenerEstadios() {
        String sql = "SELECT Id, Nombre, Ubicacion FROM Estadio";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Estadio(rs.getInt("Id"), rs.getString("Nombre"), 0, rs.getString("Ubicacion")));
    }

    public int insertarEstadio(String nombre, int capacidad, String ciudad) {
        String sql = "INSERT INTO Estadio (Nombre, Ubicacion) VALUES (?, ?)";
        return jdbcTemplate.update(sql, nombre, ciudad);
    }
}