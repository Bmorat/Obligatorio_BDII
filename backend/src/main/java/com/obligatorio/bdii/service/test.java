package com.obligatorio.bdii.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.obligatorio.bdii.repository.DatabaseQueryRepository;

@Service
public class test {
    
    private final DatabaseQueryRepository repository;

    public test(DatabaseQueryRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> obtenerEstadios() {
        String sql = "SELECT * FROM Estadio";
        return repository.executeQuery(sql);
    }

    public int insertarEstadio(String nombre, int capacidad, String ciudad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertarEstadio'");
    }
}
