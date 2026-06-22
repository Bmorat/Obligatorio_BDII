package com.obligatorio.bdii.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.obligatorio.bdii.model.Estadio;
import com.obligatorio.bdii.model.Evento;
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
    public int crearEstadio(@RequestParam String nombre,
                            @RequestParam String ubicacion,
                            @RequestParam String paisDocAdmin,
                            @RequestParam String tipoDocAdmin,
                            @RequestParam String numeroDocAdmin) {
        return estadioService.insertarEstadio(nombre, ubicacion, paisDocAdmin, tipoDocAdmin, numeroDocAdmin);
    }
     @GetMapping(params = {"paisDoc", "tipoDoc", "numeroDoc"})
    public List<Estadio> BuscarEstadioPorAdmin(
            @RequestParam String paisDoc,
            @RequestParam String tipoDoc,
            @RequestParam String numeroDoc) {
       return estadioService.BuscarEstadioPorAdmin(paisDoc, tipoDoc, numeroDoc);
    }

   @GetMapping("/{id}/eventos")
    public List<Evento> obtenerEventosPorEstadio(@PathVariable String id) {
        return estadioService.obtenerEventoPorEstadio(id);
    }

   
}