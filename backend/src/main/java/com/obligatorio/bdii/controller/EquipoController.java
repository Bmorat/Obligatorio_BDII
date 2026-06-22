package com.obligatorio.bdii.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.obligatorio.bdii.model.Equipo;
import com.obligatorio.bdii.service.EquipoService;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public List<Equipo> getEquipos() {
        return equipoService.obtenerEquipos();
    }

    @GetMapping("/evento/{idEvento}")
    public List<Equipo> getEquiposByEvento(@PathVariable Integer idEvento) {
        return equipoService.obtenerEquiposPorEvento(idEvento);
    }
}
