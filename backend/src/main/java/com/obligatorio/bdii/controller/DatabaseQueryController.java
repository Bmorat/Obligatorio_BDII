package com.obligatorio.bdii.controller;

import com.obligatorio.bdii.dto.DatabaseQueryRequest;
import com.obligatorio.bdii.dto.DatabaseQueryResponse;
import com.obligatorio.bdii.service.DatabaseQueryService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
public class DatabaseQueryController {

    private final DatabaseQueryService databaseQueryService;

    public DatabaseQueryController(DatabaseQueryService databaseQueryService) {
        this.databaseQueryService = databaseQueryService;
    }

    @PostMapping("/query")
    public DatabaseQueryResponse query(@RequestBody DatabaseQueryRequest request) {
        // Recibe el SQL desde el body y delega la validacion/ejecucion al service.
        return databaseQueryService.executeReadOnlyQuery(request.sql());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidQuery(IllegalArgumentException exception) {
        // Errores de validacion propios, por ejemplo consultas vacias o no permitidas.
        return ResponseEntity
            .badRequest()
            .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<Map<String, String>> handleSqlError(BadSqlGrammarException exception) {
        // Errores que vienen de MySQL, por ejemplo tabla o columna inexistente.
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", exception.getSQLException().getMessage()));
    }
}
