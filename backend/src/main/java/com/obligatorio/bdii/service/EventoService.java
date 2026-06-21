package com.obligatorio.bdii.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;

import com.obligatorio.bdii.model.Evento;
import com.obligatorio.bdii.model.SeHabilita;

import java.util.List;

@Service
public class EventoService {

    private final JdbcTemplate jdbcTemplate;

    public EventoService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Evento> obtenerEventos() {
        String sql = "SELECT Id, Fecha, Hora,IdEstadio, PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin FROM Evento";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Evento e= new Evento();
                e.setId(rs.getInt("Id"));
                e.setFecha(rs.getDate( "Fecha").toLocalDate()); 
                e.setHora(rs.getTime( "Hora").toLocalTime());
                e.setIdEstadio(rs.getInt( "IdEstadio"));
                e.setPaisDocAdmin(rs.getString( "PaisDocAdmin"));
                e.setTipoDocAdmin(rs.getString( "TipoDocAdmin"));
                e.setNumeroDocAdmin(rs.getString( "NumeroDocAdmin"));

                  return e;
            });
          
    }

    public int insertarEvento(LocalDate fecha, LocalTime hora, Integer idEstadio, String paisDocAdmin, String tipoDocAdmin, String numeroDocAdmin){
        String sql = "INSERT INTO Evento (fecha, hora, idEstadio, paisDocAdmin, tipoDocAdmin, numeroDocAdmin) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, fecha, hora, idEstadio, paisDocAdmin, tipoDocAdmin, numeroDocAdmin);
    }

    public List<SeHabilita> obtenerSectoresPorEvento(Integer id){
        String sql = "SELECT sh.IdEvento, sh.IdEstadio, sh.Tipo, sh.Precio, sh.CapacidadHabilitada " +
                     "FROM Se_habilita sh " +
                     "WHERE sh.IdEvento = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
               SeHabilita sh= new SeHabilita();
               sh.setIdEvento(rs.getInt("IdEvento"));
               sh.setIdEstadio(rs.getInt("IdEstadio"));
               sh.setTipo(rs.getString("Tipo"));
               sh.setPrecio(rs.getBigDecimal("Precio"));
               sh.setCapacidadHabilitada(rs.getInt("CapacidadHabilitada"));

               return sh;
        }, id);

    }

    public boolean deleteEvento(Integer idEvento) {
        String sql = "DELETE FROM Evento WHERE Id = ?";
        jdbcTemplate.update(sql, idEvento);
        return true;
    }

    public boolean updateEvento(Integer IdEvento, LocalDate Fecha, LocalTime Hora,
                                Integer IdEstadio, String PaisDocAdmin,
                                String TipoDocAdmin, String NumeroDocAdmin) {
        String sql = "UPDATE Evento SET Fecha = ?, Hora = ?, IdEstadio = ?, " +
                     "PaisDocAdmin = ?, TipoDocAdmin = ?, NumeroDocAdmin = ? WHERE Id = ?";
        jdbcTemplate.update(sql, Fecha, Hora, IdEstadio, PaisDocAdmin, TipoDocAdmin, NumeroDocAdmin, IdEvento);
        return true;
    }
}
