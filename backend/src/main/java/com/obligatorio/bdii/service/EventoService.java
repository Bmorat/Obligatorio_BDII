package com.obligatorio.bdii.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import com.obligatorio.bdii.model.Evento;
import com.obligatorio.bdii.model.SeHabilita;
import com.obligatorio.bdii.model.Sector;
import com.obligatorio.bdii.model.SectorTipo;

import java.util.List;

@Service
public class EventoService {

    private final JdbcTemplate jdbcTemplate;

    public EventoService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Evento> obtenerEventos() {
        String sql = "SELECT IdEvento, Fecha, Hora, IdEstadio FROM Evento";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Evento e = new Evento();
                e.setId(rs.getInt("IdEvento"));
                e.setFecha(rs.getDate("Fecha").toLocalDate()); 
                e.setHora(rs.getTime("Hora").toLocalTime());
                e.setIdEstadio(rs.getInt("IdEstadio"));
                return e;
            });
          
    }

    public Integer insertarEvento(LocalDate fecha, LocalTime hora, Integer idEstadio,
                                  Integer idEquipoLocal, Integer idEquipoVisitante) {
        String sqlEvento = "INSERT INTO Evento (fecha, hora, idEstadio) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlEvento, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, fecha);
            ps.setObject(2, hora);
            ps.setInt(3, idEstadio);
            return ps;
        }, keyHolder);

        Integer idEvento = keyHolder.getKey().intValue();

        String sqlJuega = "INSERT INTO Juega (IdEvento, IdEquipo, Rol) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlJuega, idEvento, idEquipoLocal, "Local");
        jdbcTemplate.update(sqlJuega, idEvento, idEquipoVisitante, "Visitante");

        return idEvento;
    }

    public List<SeHabilita> obtenerSectoresPorEvento(Integer id){
        String sql = "SELECT sh.IdEvento, sh.IdEstadio, sh.Tipo, sh.Precio, sh.CapacidadHabilitada, s.Capacidad as CapacidadMaxima " +
                     "FROM Se_habilita sh " +
                      "JOIN Sector s ON sh.IdEstadio = s.IdEstadio AND sh.Tipo = s.TipoSector " +
                     "WHERE sh.IdEvento = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
               SeHabilita sh = new SeHabilita();
               sh.setIdEvento(rs.getInt("IdEvento"));
               sh.setIdEstadio(rs.getInt("IdEstadio"));
               sh.setTipo(SectorTipo.valueOf(rs.getString("Tipo")));
               sh.setPrecio(rs.getBigDecimal("Precio"));
               sh.setCapacidadHabilitada(rs.getInt("CapacidadHabilitada"));
               return sh;
        }, id);

    }

    public List<Sector> obtenerSectoresPorEstadio(Integer idEstadio){
        String sql = "SELECT IdEstadio, TipoSector, Capacidad FROM Sector WHERE IdEstadio = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Sector s = new Sector();
            s.setIdEstadio(rs.getInt("IdEstadio"));
            s.setTipo(SectorTipo.valueOf(rs.getString("TipoSector")));
            s.setCapacidad(rs.getInt("Capacidad"));
            return s;
        }, idEstadio);
    }

    public boolean deleteEvento(Integer idEvento) {
        jdbcTemplate.update("DELETE FROM Asignado_a WHERE IdEvento = ?", idEvento);
        jdbcTemplate.update("DELETE FROM Transferencia WHERE IdEntrada IN (SELECT IdEntrada FROM Entrada WHERE IdEvento = ?)", idEvento);
        jdbcTemplate.update("DELETE FROM Entrada WHERE IdEvento = ?", idEvento);
        jdbcTemplate.update("DELETE FROM Se_habilita WHERE IdEvento = ?", idEvento);
        jdbcTemplate.update("DELETE FROM Juega WHERE IdEvento = ?", idEvento);
        jdbcTemplate.update("DELETE FROM Evento WHERE IdEvento = ?", idEvento);
        return true;
    }

    public boolean updateEvento(Integer IdEvento, LocalDate Fecha, LocalTime Hora,
                                Integer IdEstadio,
                                Integer idEquipoLocal, Integer idEquipoVisitante) {
        String sql = "UPDATE Evento SET Fecha = ?, Hora = ?, IdEstadio = ? WHERE IdEvento = ?";
        jdbcTemplate.update(sql, Fecha, Hora, IdEstadio, IdEvento);

        if (idEquipoLocal != null && idEquipoVisitante != null) {
            jdbcTemplate.update("DELETE FROM Juega WHERE IdEvento = ?", IdEvento);
            jdbcTemplate.update("INSERT INTO Juega (IdEvento, IdEquipo, Rol) VALUES (?, ?, ?)", IdEvento, idEquipoLocal, "Local");
            jdbcTemplate.update("INSERT INTO Juega (IdEvento, IdEquipo, Rol) VALUES (?, ?, ?)", IdEvento, idEquipoVisitante, "Visitante");
        }

        return true;
    }
}
