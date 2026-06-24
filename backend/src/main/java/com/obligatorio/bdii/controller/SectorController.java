package com.obligatorio.bdii.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.obligatorio.bdii.model.Sector;
import com.obligatorio.bdii.service.EventoService;

@RestController
@RequestMapping("/api/sectores")
public class SectorController {

    private final EventoService eventoService;

    public SectorController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public List<Sector> obtenerSectoresPorEstadio(@RequestParam Integer estadioId) {
        return eventoService.obtenerSectoresPorEstadio(estadioId);
    }
}
