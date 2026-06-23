package com.obligatorio.bdii.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.dto.EventoReporteDTO;
import com.obligatorio.bdii.dto.MayorCompradorDTO;

@Service
public class ReporteService {

    private final JdbcTemplate jdbcTemplate;

    public ReporteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EventoReporteDTO> eventosMasVendidos() {
        String sql = """
                SELECT
                    ev.Id          AS idEvento,
                    ev.Fecha       AS fecha,
                    ev.Hora        AS hora,
                    COUNT(en.IdEntrada) AS totalEntradas,
                    SUM(sh.Precio * (1 + co.Porcentaje / 100)) AS totalRemunerado
                FROM Evento ev
                JOIN Entrada en ON en.IdEvento = ev.Id
                JOIN Se_habilita sh ON sh.IdEvento = en.IdEvento
                                   AND sh.IdEstadio = en.IdEstadio
                                   AND sh.Tipo = en.Tipo
                JOIN Compra c ON c.Id = en.IdCompra
                JOIN Comision co ON co.IdComision = c.IdComision
                GROUP BY ev.Id
                ORDER BY totalEntradas DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new EventoReporteDTO(
                rs.getInt("idEvento"),
                rs.getDate("fecha").toLocalDate(),
                rs.getTime("hora").toLocalTime(),
                rs.getInt("totalEntradas"),
                rs.getBigDecimal("totalRemunerado").setScale(2, RoundingMode.HALF_UP),
                null
        ));
    }

    public List<MayorCompradorDTO> mayoresCompradores() {
        String sql = """
                SELECT
                    u.Correo,
                    u.PaisDoc,
                    u.TipoDoc,
                    u.NumeroDoc,
                    COUNT(en.IdEntrada) AS totalEntradas,
                    (SELECT SUM(c2.MontoTotal)
                     FROM Compra c2
                     WHERE c2.PaisDocUsuario = u.PaisDoc
                       AND c2.TipoDocUsuario = u.TipoDoc
                       AND c2.NumeroDocUsuario = u.NumeroDoc) AS totalGastado
                FROM Usuario u
                JOIN Compra c ON c.PaisDocUsuario = u.PaisDoc
                             AND c.TipoDocUsuario = u.TipoDoc
                             AND c.NumeroDocUsuario = u.NumeroDoc
                JOIN Entrada en ON en.IdCompra = c.Id
                GROUP BY u.PaisDoc, u.TipoDoc, u.NumeroDoc, u.Correo
                ORDER BY totalEntradas DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MayorCompradorDTO(
                rs.getString("Correo"),
                rs.getString("PaisDoc"),
                rs.getString("TipoDoc"),
                rs.getString("NumeroDoc"),
                rs.getInt("totalEntradas"),
                rs.getBigDecimal("totalGastado").setScale(2, RoundingMode.HALF_UP)
        ));
    }
}
