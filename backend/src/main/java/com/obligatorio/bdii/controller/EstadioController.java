package com.obligatorio.bdii.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.obligatorio.bdii.model.Estadio;
import com.obligatorio.bdii.service.EstadioService;

@RestController
@RequestMapping("/api/estadios")
public class EstadioController {
    
    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @GetMapping
    public List<Estadio> obtenerEstadios() {
        return estadioService.obtenerEstadios();
    }

    @PostMapping
    public int crearEstadio(@RequestParam String nombre, @RequestParam int capacidad, @RequestParam String ciudad) {
        return estadioService.insertarEstadio(nombre, capacidad, ciudad);
    }
}