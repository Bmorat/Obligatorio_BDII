package com.obligatorio.bdii.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.obligatorio.bdii.dto.CompraRequest;
import org.springframework.stereotype.Service;

@Service
public class CompraService {
    
    private final JdbcTemplate jdbcTemplate;

    public CompraService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<?> comprar(CompraRequest request) {
        // Validar que cantidad no sea mayor a 5
        if(request.cantidad() > 5){
            return ResponseEntity.badRequest().body("No podes comprar mas de 5 entradas");
        }

        //Obtenemos el id de la comision vigente para insertar en la compra
        Integer idComision = jdbcTemplate.queryForObject(
            "SELECT idComision FROM Comision Where Vigente = true LIMIT 1",
            Integer.class);
        
        //Obtenemos el porcenaje para calcular el precio total
        BigDecimal porcentaje = jdbcTemplate.queryForObject(
            "SELECT Porcentaje FROM Comision WHERE Vigente = true LIMIT 1",
            BigDecimal.class);
        
        //Obtenemos el precio del evento-sector
        String sql = "SELECT Precio FROM Se_habilita WHERE idEvento = ? AND idEstadio = ? AND Tipo = ?";
        BigDecimal precio = jdbcTemplate.queryForObject(sql, BigDecimal.class, request.idEvento(), request.idEstadio(), request.tipo());
        
        // Calculamos el total de la compra con la cantidad y la comision
        BigDecimal subTotal = precio.multiply(BigDecimal.valueOf(request.cantidad()));
        BigDecimal total    = subTotal.add(subTotal.multiply(porcentaje.divide(BigDecimal.valueOf(100))));

        //Insertamos la compra
        String sqlCompra = "INSERT INTO Compra (Estado, Fecha, MontoTotal, PaisDocUsuario, TipoDocUsuario, NumeroDocUsuario, IdComision) VALUES (?,?,?,?,?,?,?)";
        //El KeyHolder es para capturar el AUTO_INCREMENT que MySQL genera.
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "Confirmada");
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setBigDecimal(3,total);
            ps.setString(4, request.paisDocUsuario());
            ps.setString(5, request.tipoDocUsuario());
            ps.setString(6, request.numeroDocUsuario());
            ps.setInt(7, (int) idComision);
            return ps;
        }, keyHolder);

        int idCompra = keyHolder.getKey().intValue();

        for (int i = 0; i<request.cantidad(); i++){
            String qr = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO QR (IdQR) VALUES (?)", qr);
            jdbcTemplate.update(
                "INSERT INTO Entrada (Estado, NumeroVecesTransferida, idCompra, idEvento, idEstadio, Tipo, IdQR) VALUES (?,?,?,?,?,?,?)",
                "Activa", 0 , idCompra , request.idEvento(), request.idEstadio(), request.tipo(), qr
            );
            
        }
        return ResponseEntity.ok("Compra realizada con exito");
    }

}
