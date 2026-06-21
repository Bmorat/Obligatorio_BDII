package com.obligatorio.bdii.controller;

import com.obligatorio.bdii.model.SeHabilita;
import com.obligatorio.bdii.service.seHablitaService;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/SeHablita")
public class SeHablitaController {

    private final seHablitaService seHablitaService;

    public SeHablitaController(seHablitaService seHablitaService) {
        this.seHablitaService = seHablitaService;
    }

    @RequestMapping
    public boolean setSeHablitado(Integer IdEvento, String Tipo, Integer Precio, Integer CapacidadMax) {
        return seHablitaService.setSeHablitado(IdEvento, Tipo, Precio, CapacidadMax);
    }

    @RequestMapping
    public boolean deleteSeHablitado(Integer IdEvento, String Tipo) {
        return deleteSeHablitado(IdEvento, Tipo);
    }

    @RequestMapping
    public List<SeHabilita> getSeHablita(Integer IdEvento) {
        return seHablitaService.getSeHablita(IdEvento);
    }
}
