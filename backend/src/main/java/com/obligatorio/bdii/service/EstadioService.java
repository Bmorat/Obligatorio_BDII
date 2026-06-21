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
    public List<Evento> obtenerEventoPorEstadio(String idEstadio) {
        String sql = "SELECT ev.Id, ev.Fecha, ev.Hora, ev.IdEstadio, ev.PaisDocAdmin, ev.TipoDocAdmin, ev.NumeroDocAdmin "
                +
                "FROM Evento ev WHERE ev.IdEstadio = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Evento(
                rs.getInt("Id"),
                rs.getDate("Fecha").toLocalDate(),
                rs.getTime("Hora").toLocalTime(),
                rs.getInt("IdEstadio"),
                rs.getString("PaisDocAdmin"),
                rs.getString("TipoDocAdmin"),
                rs.getString("NumeroDocAdmin")), idEstadio);
    }


     /**
     * Conencta a Admin a Evento, luego va a estadio. Obtiene todos los estadios que se conectan a ese admin.
     * @param paisDoc
     * @param tipoDoc
     * @param numeroDoc
     * @return Retorna una lista de estadios
     */
    public List<Estadio> BuscarEstadioPorAdmin(String paisDoc, String tipoDoc, String numeroDoc) {
        String sql = "SELECT DISTINCT s.Id, s.Nombre, s.Ubicacion " +
                     "FROM Administrador_Sede a " +
                     "INNER JOIN Evento e ON a.PaisDoc = e.PaisDocAdmin " +
                     "AND a.TipoDoc = e.TipoDocAdmin " +
                     "AND a.NumeroDoc = e.NumeroDocAdmin " +
                     "INNER JOIN Estadio s ON s.Id = e.IdEstadio " +
                     "WHERE a.PaisDoc = ? " +
                     "AND a.TipoDoc = ? " +
                     "AND a.NumeroDoc = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Estadio(
                rs.getInt("Id"),
                rs.getString("Nombre"),
                rs.getString("Ubicacion")
            ), paisDoc, tipoDoc, numeroDoc);
    }
}
    