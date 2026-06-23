package com.obligatorio.bdii.controller;

import com.obligatorio.bdii.model.SeHabilita;
import com.obligatorio.bdii.model.SectorTipo;
import com.obligatorio.bdii.service.seHablitaService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/SeHablita")
public class SeHablitaController {

    private final seHablitaService seHablitaService;

    public SeHablitaController(seHablitaService seHablitaService) {
        this.seHablitaService = seHablitaService;
    }

    @PostMapping
    public boolean setSeHablitado(@RequestParam Integer IdEvento, @RequestParam SectorTipo Tipo, @RequestParam Integer Precio, @RequestParam Integer CapacidadMax) {
        return seHablitaService.setSeHablitado(IdEvento, Tipo, Precio, CapacidadMax);
    }

    @DeleteMapping
    public boolean deleteSeHablitado(@RequestParam("IdEvento") Integer idEvento, @RequestParam("Tipo") SectorTipo tipo) {
        return seHablitaService.deleteSeHablitado(idEvento, tipo);
    }

    @GetMapping("{idEvento}")
    public List<SeHabilita> getSeHablita(@PathVariable Integer idEvento) {
    return seHablitaService.getSeHablita(idEvento);
    }
}
