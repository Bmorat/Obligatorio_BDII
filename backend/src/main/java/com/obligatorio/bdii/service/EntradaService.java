package com.obligatorio.bdii.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

import com.obligatorio.bdii.model.Entrada;

@Service
public class EntradaService {

    private final JdbcTemplate jdbcTemplate;

    public EntradaService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Entrada> obtenerEntradasPorUsuario(String paisDocUsuario, String tipoDocUsuario, String numeroDocUsuario){
        String sql = "SELECT e.IdEntrada, e.Estado, e.NumeroVecesTransferida, e.IdCompra, e.IdEvento, e.IdEstadio, e.Tipo, e.IdQR " +
         "FROM Entrada e "+ 
         "JOIN Compra c ON e.IdCompra = c.Id "+
         "WHERE c.PaisDocUsuario = ? AND c.TipoDocUsuario = ? AND c.NumeroDocUsuario = ?";
    
         return jdbcTemplate.query(sql,(rs,rowNum)->{
            Entrada e = new Entrada();
            e.setIdEntrada(rs.getInt("idEntrada"));
            e.setEstado(rs.getString("estado"));
            e.setNumeroVecesTransferida(rs.getInt("numeroVecesTransferida"));
            e.setIdCompra(rs.getInt("idCompra"));
            e.setIdEvento(rs.getInt("idEvento"));
            e.setIdEstadio(rs.getInt("idEstadio"));
            e.setTipo(rs.getString("tipo"));
            e.setIdQR(rs.getString("idQR"));
            return e;
         },paisDocUsuario,tipoDocUsuario,numeroDocUsuario);
        }
    

}
