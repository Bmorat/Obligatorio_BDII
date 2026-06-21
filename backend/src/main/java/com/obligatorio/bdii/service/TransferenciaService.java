package com.obligatorio.bdii.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.dto.TransferenciaRequest;
import com.obligatorio.bdii.model.Transferencia;

@Service
public class TransferenciaService {

    private final JdbcTemplate jdbcTemplate;

    public TransferenciaService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public ResponseEntity<?> iniciarTransferencia(TransferenciaRequest request){
        //Verificar que la entrada existe
        String sql = "SELECT Estado FROM Entrada e WHERE e.idEntrada = ?";
        String estado = jdbcTemplate.queryForObject(sql, String.class, request.idEntrada());
        
        if(!"Activa".equals(estado)){
            return ResponseEntity.badRequest().body("La entrada no esta disponible para transferir");
        }

        String sqlInsert = "INSERT INTO Transferencia (FechaTransferencia, EstadoTransferencia, idEntrada, PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert,
            LocalDate.now(),
            "Pendiente",
            request.idEntrada(),
            request.paisDocDestinatario(),
            request.tipoDocDestinatario(),
            request.numeroDocDestinatario()
        );
        return ResponseEntity.ok("Transferencia iniciado con exito"); 
    }

    public ResponseEntity<?> aceptarTransferencia(Integer id) {
        // Verificar que existe y esta pendiente
        String sqlCheck = "SELECT EstadoTransferencia FROM Transferencia WHERE idTransferencia = ? ";
        String estado = jdbcTemplate.queryForObject(sqlCheck,String.class, id);
        
        if(!"Pendiente".equals(estado)){
            return ResponseEntity.badRequest().body("La transferncia no esta pendiente de ser aceptada");
        }

        //Obtener el idEntrada de esta transferencia
        Integer idEntrada = jdbcTemplate.queryForObject(
            "SELECT IdEntrada FROM Transferencia WHERE IdTransferencia = ?",
            Integer.class, id);
        
        //Verificar que no halla alcanzado el limite de transferencias
        Integer vecesTransferida = jdbcTemplate.queryForObject(
            "SELECT NumeroVecesTransferida FROM Entrada WHERE IdEntrada = ?", 
            Integer.class, idEntrada);
        
        if(vecesTransferida>=3){
            return ResponseEntity.badRequest().body("Esta entrada ya alcanzo el maximo de veces transferida");
        }
        
        //Actualizar la transferencia a aceptada
        jdbcTemplate.update(
            "UPDATE Transferencia SET EstadoTransferencia = 'Aceptada' WHERE idTransferencia = ?", id);
        
        //Actualizar numero de veces transferida a la entrada
        jdbcTemplate.update("UPDATE Entrada SET Estado = 'Transferida', NumeroVecesTransferida = NumeroVecesTransferida + 1 WHERE idEntrada = ?",idEntrada);

        return ResponseEntity.ok("Transferencia aceptada");
    }

    public ResponseEntity<?> rechazarTransferencia(Integer id){
        jdbcTemplate.update("UPDATE Transferencia SET EstadoTransferencia = 'Rechazada' WHERE idTransferencia = ?", id);

        return ResponseEntity.ok("Transferencia rechazada con exito");
    }

    public List<Transferencia> obtenerTransferenciasRecibidas(String paisDoc, String tipoDoc, String numeroDoc) {
        String sql = "SELECT idTransferencia, FechaTransferencia, EstadoTransferencia, idEntrada, PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario " +
                     "FROM Transferencia WHERE PaisDocUsuario = ? AND TipoDocUsuario = ? AND NumeroDocUsuario = ?";
        return jdbcTemplate.query(sql, (rs, rowNum)->{
            Transferencia t = new Transferencia();
            t.setIdTransferencia(rs.getInt("IdTransferencia"));
            t.setFechaTransferencia(rs.getDate("FechaTransferencia").toLocalDate());
            t.setEstadoTransferencia(rs.getString("EstadoTransferencia"));
            t.setIdEntrada(rs.getInt("IdEntrada"));
            t.setPaisDocUsuario(rs.getString("PaisDocUsuario"));
            t.setTipoDocUsuario(rs.getString("TipoDocUsuario"));
            t.setNumeroDocUsuario(rs.getString("NumeroDocUsuario"));
            return t;
        },paisDoc, tipoDoc, numeroDoc);
    }
}
