package com.obligatorio.bdii.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.obligatorio.bdii.model.Evento;
import com.obligatorio.bdii.model.SeHabilita;
import com.obligatorio.bdii.service.EventoService;


@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    
    private final EventoService eventoService;

    public EventoController(EventoService eventoService){
        this.eventoService= eventoService;
    }

    @GetMapping
    public List<Evento> obtenerEventos() {
        return eventoService.obtenerEventos();
    }

    @PostMapping
    public Integer crearEvento(@RequestParam LocalDate fecha,
                               @RequestParam LocalTime hora,
                               @RequestParam Integer idEstadio,
                               @RequestParam Integer idEquipoLocal,
                               @RequestParam Integer idEquipoVisitante) {

        return eventoService.insertarEvento(fecha, hora, idEstadio, idEquipoLocal, idEquipoVisitante);
    }
    
    @GetMapping("/{id}/sectores")
    public List<SeHabilita> obtenerSectores(@PathVariable Integer id) {
        return eventoService.obtenerSectoresPorEvento(id);
    }
    @DeleteMapping("/{id}")
    public boolean deleteEvento(@PathVariable Integer id) {
        return eventoService.deleteEvento(id);
    }

    @PatchMapping("/{id}")
    public boolean updateEvento(@PathVariable Integer id,
                                @RequestParam LocalDate fecha,
                                @RequestParam LocalTime hora,
                                @RequestParam Integer idEstadio,
                                @RequestParam(required = false) Integer idEquipoLocal,
                                @RequestParam(required = false) Integer idEquipoVisitante) {
        return eventoService.updateEvento(id, fecha, hora, idEstadio, idEquipoLocal, idEquipoVisitante);
    }

    
    



}
