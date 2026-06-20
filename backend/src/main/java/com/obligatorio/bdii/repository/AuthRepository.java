package com.obligatorio.bdii.repository;

import com.obligatorio.bdii.dto.AuthenticatedUser;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<AuthenticatedUser> findByCorreo(String correo) {
        var usuarios = jdbcTemplate.query("""
                SELECT
                    u.Correo,
                    u.PaisDoc,
                    u.TipoDoc,
                    u.NumeroDoc,
                    c.PasswordHash
                FROM Usuario u
                JOIN CredencialUsuario c
                  ON c.PaisDoc = u.PaisDoc
                 AND c.TipoDoc = u.TipoDoc
                 AND c.NumeroDoc = u.NumeroDoc
                WHERE u.Correo = ?
                """,
                (rs, rowNum) -> new AuthenticatedUser(
                        rs.getString("Correo"),
                        rs.getString("PaisDoc"),
                        rs.getString("TipoDoc"),
                        rs.getString("NumeroDoc"),
                        rs.getString("PasswordHash"),
                        null),
                correo);
        if (usuarios.isEmpty()) {
            return Optional.empty();
        }

        AuthenticatedUser user = usuarios.get(0);
        String rol = findRol(user.paisDoc(), user.tipoDoc(), user.numeroDoc());

        return Optional.of(new AuthenticatedUser(
                user.correo(),
                user.paisDoc(),
                user.tipoDoc(),
                user.numeroDoc(),
                user.passwordHash(),
                rol));
    }

    private String findRol(String paisDoc, String tipoDoc, String numeroDoc) {
        if (existsIn("Administrador_Sede", paisDoc, tipoDoc, numeroDoc)) {
            return "ROLE_ADMIN_SEDE";
        }

        if (existsIn("Funcionario_Validacion", paisDoc, tipoDoc, numeroDoc)) {
            return "ROLE_FUNCIONARIO";
        }

        if (existsIn("Usuario_General", paisDoc, tipoDoc, numeroDoc)) {
            return "ROLE_USUARIO";
        }

        throw new IllegalStateException("El usuario no tiene tipo asignado");
    }

    private boolean existsIn(String tableName, String paisDoc, String tipoDoc, String numeroDoc) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + tableName + " WHERE PaisDoc = ? AND TipoDoc = ? AND NumeroDoc = ?",
                Integer.class,
                paisDoc,
                tipoDoc,
                numeroDoc);

        return count != null && count > 0;
    }
}
