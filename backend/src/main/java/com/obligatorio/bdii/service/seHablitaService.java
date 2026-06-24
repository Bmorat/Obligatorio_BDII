package com.obligatorio.bdii.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.model.SeHabilita;
import com.obligatorio.bdii.model.SectorTipo;

@Service
public class seHablitaService {

    private final JdbcTemplate jdbcTemplate;

    public seHablitaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean setSeHablitado(Integer IdEvento, SectorTipo Tipo, Integer Precio, Integer CapacidadMax) {
        Integer IdEstadio = jdbcTemplate.queryForObject("select IdEstadio from Evento where Id = ?", Integer.class, IdEvento);
        String sql = "INSERT INTO Se_habilita (IdEvento, IdEstadio, Tipo, Precio, CapacidadHabilitada) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, IdEvento, IdEstadio, Tipo.name(), Precio, CapacidadMax);
        return true; 
    }
    public boolean deleteSeHablitado(Integer IdEvento, SectorTipo Tipo) {
        String sql = "DELETE FROM Se_habilita WHERE IdEvento = ? AND Tipo = ?";
        jdbcTemplate.update(sql, IdEvento, Tipo.name());
        return true;
    }

    public boolean forceDeleteSeHablitado(Integer IdEvento, SectorTipo Tipo) {
        Integer IdEstadio = jdbcTemplate.queryForObject("SELECT IdEstadio FROM Evento WHERE Id = ?", Integer.class, IdEvento);

        jdbcTemplate.update("DELETE FROM Asignado_a WHERE IdEvento = ? AND Tipo = ?",
                IdEvento, Tipo.name());
        jdbcTemplate.update("DELETE FROM Transferencia WHERE IdEntrada IN " +
                        "(SELECT IdEntrada FROM Entrada WHERE IdEvento = ? AND IdEstadio = ? AND Tipo = ?)",
                IdEvento, IdEstadio, Tipo.name());
        jdbcTemplate.update("DELETE FROM Entrada WHERE IdEvento = ? AND IdEstadio = ? AND Tipo = ?",
                IdEvento, IdEstadio, Tipo.name());
        jdbcTemplate.update("DELETE FROM Se_habilita WHERE IdEvento = ? AND Tipo = ?",
                IdEvento, Tipo.name());
        return true;
    }

    public List<SeHabilita> getSeHablita(Integer idEvento) {
        String sql = "SELECT IdEvento, IdEstadio, Tipo, Precio, CapacidadHabilitada FROM Se_habilita WHERE IdEvento = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new SeHabilita(
                rs.getInt("IdEvento"),
                rs.getInt("IdEstadio"),
                SectorTipo.valueOf(rs.getString("Tipo")),
                rs.getBigDecimal("Precio"),
                rs.getInt("CapacidadHabilitada")
        ), idEvento);
    }


}
    