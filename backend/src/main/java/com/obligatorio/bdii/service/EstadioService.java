package com.obligatorio.bdii.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.model.Estadio;
import com.obligatorio.bdii.model.Evento;

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

    
    /**
     * Se usa para obtener todos los eventos que tiene un estadio.
     * @param idEstadio
     * @return Lista de eventos de un determinado estadio.
     */
    public List<Evento> obtenerEventoCsPorEstadio(String idEstadio) {
        String sql = "SELECT ev.Id, ev.Fecha, ev.Hora, ev.IdEstadio, ev.PaisDocAdmin, ev.TipoDocAdmin, ev.NumeroDocAdmin " +
                     "FROM Evento ev WHERE ev.IdEstadio = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Evento(
                rs.getInt("Id"),
                rs.getDate("Fecha").toLocalDate(),
                rs.getTime("Hora").toLocalTime(),
                rs.getInt("IdEstadio"),
                rs.getString("PaisDocAdmin"),
                rs.getString("TipoDocAdmin"),
                rs.getString("NumeroDocAdmin")
            ), idEstadio);
    }
}