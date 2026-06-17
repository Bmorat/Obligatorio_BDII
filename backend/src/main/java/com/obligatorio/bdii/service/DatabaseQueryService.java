package com.obligatorio.bdii.service;

import com.obligatorio.bdii.dto.DatabaseQueryResponse;
import com.obligatorio.bdii.repository.DatabaseQueryRepository;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DatabaseQueryService {

    // Limite defensivo para evitar requests enormes.
    private static final int MAX_QUERY_LENGTH = 10_000;

    private final DatabaseQueryRepository databaseQueryRepository;

    public DatabaseQueryService(DatabaseQueryRepository databaseQueryRepository) {
        this.databaseQueryRepository = databaseQueryRepository;
    }

    public DatabaseQueryResponse executeReadOnlyQuery(String sql) {
        String normalizedSql = normalizeAndValidate(sql);

        // Ejecuta la consulta mediante el repository y devuelve cada fila como columna -> valor.
        List<Map<String, Object>> rows = databaseQueryRepository.executeQuery(normalizedSql);

        // Si hay filas, toma los nombres de columnas desde la primera fila.
        List<String> columns = rows.isEmpty()
            ? List.of()
            : List.copyOf(rows.getFirst().keySet());

        return new DatabaseQueryResponse(columns, rows, rows.size());
    }

    private String normalizeAndValidate(String sql) {
        if (!StringUtils.hasText(sql)) {
            throw new IllegalArgumentException("La consulta SQL no puede estar vacia.");
        }

        // Trim y remocion de un punto y coma final para aceptar consultas copiadas de DataGrip.
        String normalizedSql = sql.trim();
        if (normalizedSql.endsWith(";")) {
            normalizedSql = normalizedSql.substring(0, normalizedSql.length() - 1).trim();
        }

        if (normalizedSql.length() > MAX_QUERY_LENGTH) {
            throw new IllegalArgumentException("La consulta SQL es demasiado larga.");
        }

        String lowerCaseSql = normalizedSql.toLowerCase(Locale.ROOT);

        // Por ahora el manipulador es solo de lectura para no modificar la BD por accidente.
        if (!isReadOnlyQuery(lowerCaseSql)) {
            throw new IllegalArgumentException("Solo se permiten consultas de lectura: SELECT, SHOW, DESCRIBE o EXPLAIN.");
        }

        // Bloquea multiples sentencias y comentarios, que complican la validacion del SQL.
        if (containsUnsafeToken(lowerCaseSql)) {
            throw new IllegalArgumentException("La consulta no puede contener multiples sentencias ni comentarios SQL.");
        }

        return normalizedSql;
    }

    private boolean isReadOnlyQuery(String lowerCaseSql) {
        return lowerCaseSql.startsWith("select ")
            || lowerCaseSql.startsWith("show ")
            || lowerCaseSql.startsWith("describe ")
            || lowerCaseSql.startsWith("desc ")
            || lowerCaseSql.startsWith("explain ");
    }

    private boolean containsUnsafeToken(String lowerCaseSql) {
        return lowerCaseSql.contains(";")
            || lowerCaseSql.contains("--")
            || lowerCaseSql.contains("/*")
            || lowerCaseSql.contains("*/")
            || lowerCaseSql.contains("#");
    }
}
