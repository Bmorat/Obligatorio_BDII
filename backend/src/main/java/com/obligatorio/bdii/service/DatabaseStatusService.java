package com.obligatorio.bdii.service;

import com.obligatorio.bdii.dto.DatabaseStatusResponse;
import com.obligatorio.bdii.repository.DatabaseStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class DatabaseStatusService {

    private final DatabaseStatusRepository databaseStatusRepository;

    public DatabaseStatusService(DatabaseStatusRepository databaseStatusRepository) {
        this.databaseStatusRepository = databaseStatusRepository;
    }

    public DatabaseStatusResponse getStatus() {
        String databaseName = databaseStatusRepository.getCurrentDatabaseName();
        Integer tablesCount = databaseStatusRepository.countCurrentDatabaseTables();

        return new DatabaseStatusResponse(true, databaseName, tablesCount);
    }
}
