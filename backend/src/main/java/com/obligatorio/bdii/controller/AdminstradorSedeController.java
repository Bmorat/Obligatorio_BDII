package com.obligatorio.bdii.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obligatorio.bdii.model.Estadio;
import com.obligatorio.bdii.service.AdministradorSedeService;

@RestController
@RequestMapping("/api/admin/estadios")
public class AdminstradorSedeController {
    private final AdministradorSedeService administradorSedeService;

    public AdminstradorSedeController(AdministradorSedeService administradorSedeService) {
        this.administradorSedeService = administradorSedeService;
    }

    @GetMapping
    public List<Estadio> BuscarEstadioPorAdmin(
            @RequestParam String paisDoc,
            @RequestParam String tipoDoc,
            @RequestParam String numeroDoc) {
       return administradorSedeService.BuscarEstadioPorAdmin(paisDoc, tipoDoc, numeroDoc);
    }

}
