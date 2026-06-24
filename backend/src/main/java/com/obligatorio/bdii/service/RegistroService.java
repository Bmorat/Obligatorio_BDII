package com.obligatorio.bdii.service;

import java.time.LocalDate;
import java.sql.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.obligatorio.bdii.dto.RegistroRequest;

@Service
public class RegistroService {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public RegistroService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> registrar(RegistroRequest request) {
        // Verificar que el correo no esté en uso
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Usuario WHERE Correo = ?",
            Integer.class,
            request.correo()
        );
        if (count != null && count > 0) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        // Crear el código postal si no existe
        jdbcTemplate.update(
            "INSERT IGNORE INTO CodigoPostal (Codigo, Localidad, Pais) VALUES (?, ?, ?)",
            request.codigoPostal(),
            request.localidad(),
            request.paisCP()
        );

        // Insertar el usuario
        jdbcTemplate.update(
            "INSERT INTO Usuario (PaisDoc, TipoDoc, NumeroDoc, Correo, Dir_Calle, Dir_Numero, CodigoPostal) VALUES (?, ?, ?, ?, ?, ?, ?)",
            request.paisDoc(),
            request.tipoDoc(),
            request.numeroDoc(),
            request.correo(),
            request.dirCalle(),
            request.dirNumero(),
            request.codigoPostal()
        );

        // Insertar la credencial con password hasheado
        String hash = passwordEncoder.encode(request.password());
        jdbcTemplate.update(
            "INSERT INTO CredencialUsuario (PaisDoc, TipoDoc, NumeroDoc, PasswordHash) VALUES (?, ?, ?, ?)",
            request.paisDoc(),
            request.tipoDoc(),
            request.numeroDoc(),
            hash
        );

        // Insertar como Usuario_General
        jdbcTemplate.update(
            "INSERT INTO Usuario_General (PaisDoc, TipoDoc, NumeroDoc, FechaRegistro, EstadoVerificacion) VALUES (?, ?, ?, ?, ?)",
            request.paisDoc(),
            request.tipoDoc(),
            request.numeroDoc(),
            Date.valueOf(LocalDate.now()),
            false
        );

        // Insertar los teléfonos
        for (String telefono : request.telefonos()) {
            jdbcTemplate.update(
                "INSERT INTO Telefono (PaisDoc, TipoDoc, NumeroDoc, NumTelefono) VALUES (?, ?, ?, ?)",
                request.paisDoc(),
                request.tipoDoc(),
                request.numeroDoc(),
                telefono
            );
        }

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
