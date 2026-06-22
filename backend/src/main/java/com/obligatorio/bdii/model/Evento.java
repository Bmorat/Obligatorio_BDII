package com.obligatorio.bdii.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @ToString @AllArgsConstructor
public class Evento {

    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer idEstadio;

    public Evento() {}
}
