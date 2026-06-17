package com.obligatorio.bdii.controller;

import com.obligatorio.bdii.dto.DatabaseStatusResponse;
import com.obligatorio.bdii.service.DatabaseStatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseStatusController {

    private final DatabaseStatusService databaseStatusService;

    public DatabaseStatusController(DatabaseStatusService databaseStatusService) {
        this.databaseStatusService = databaseStatusService;
    }

    @GetMapping("/api/helloworld")
    public String helloWorld() {
        return "helloworld";
    }

    @GetMapping("/api/database/status")
    public DatabaseStatusResponse databaseStatus() {
        return databaseStatusService.getStatus();
    }
}
