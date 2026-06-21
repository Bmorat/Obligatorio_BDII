package com.obligatorio.bdii.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.model.Estadio;

@Service
public class AdministradorSedeService {

    private final JdbcTemplate jdbcTemplate;

    public AdministradorSedeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
