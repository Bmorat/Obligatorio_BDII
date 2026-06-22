package com.obligatorio.bdii.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.obligatorio.bdii.model.Equipo;
import java.util.List;

@Service
public class EquipoService {

    private final JdbcTemplate jdbcTemplate;

    public EquipoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Equipo> obtenerEquiposPorEvento(Integer idEvento) {
        String sql = "SELECT e.Id, e.NombreDeEquipo FROM Equipo e " +
                     "INNER JOIN Juega j ON e.Id = j.IdEquipo " +
                     "WHERE j.IdEvento = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Equipo eq = new Equipo();
            eq.setId(rs.getInt("Id"));
            eq.setNombreDeEquipo(rs.getString("NombreDeEquipo"));
            return eq;
        }, idEvento);
    }
}
