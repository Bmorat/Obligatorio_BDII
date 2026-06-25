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
        String sql = "SELECT Id, Nombre, Ubicacion, PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin FROM Estadio";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Estadio e = new Estadio();
            e.setId(rs.getInt("Id"));
            e.setNombre(rs.getString("Nombre"));
            e.setUbicacion(rs.getString("Ubicacion"));
            e.setPaisDocAdmin(rs.getString("PaisDocAdmin"));
            e.setTipoDocAdmin(rs.getString("TipoDocAdmin"));
            e.setNumeroDocAdmin(rs.getString("NumeroDocAdmin"));
            return e;
        });
    }

    public int insertarEstadio(String nombre, String ubicacion, String paisDocAdmin, String tipoDocAdmin,
            String numeroDocAdmin) {
        String sql = "INSERT INTO Estadio (Nombre, Ubicacion, PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, nombre, ubicacion, paisDocAdmin, tipoDocAdmin, numeroDocAdmin);
    }

    /**
     * Se usa para obtener todos los eventos que tiene un estadio.
     * @param idEstadio
     * @return Lista de eventos de un determinado estadio.
     */
    public List<Evento> obtenerEventoPorEstadio(String idEstadio) {
        String sql = "SELECT ev.IdEvento, ev.Fecha, ev.Hora, ev.IdEstadio FROM Evento ev WHERE ev.IdEstadio = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Evento(
                rs.getInt("IdEvento"),
                rs.getDate("Fecha").toLocalDate(),
                rs.getTime("Hora").toLocalTime(),
                rs.getInt("IdEstadio")), idEstadio);
    }

    /**
    * Conencta a Admin a Evento, luego va a estadio. Obtiene todos los estadios que se conectan a ese admin.
    * @param paisDoc
    * @param tipoDoc
    * @param numeroDoc
    * @return Retorna una lista de estadios
    */
    public List<Estadio> BuscarEstadioPorAdmin(String paisDoc, String tipoDoc, String numeroDoc) {
        String sql = "SELECT Id, Nombre, Ubicacion, PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin " +
                "FROM Estadio WHERE PaisDocAdmin = ? AND TipoDocAdmin = ? AND NumeroDocAdmin = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Estadio(
                rs.getInt("Id"),
                rs.getString("Nombre"),
                rs.getString("Ubicacion"),
                rs.getString("PaisDocAdmin"),
                rs.getString("TipoDocAdmin"),
                rs.getString("NumeroDocAdmin")), paisDoc, tipoDoc, numeroDoc);
    }

}
    