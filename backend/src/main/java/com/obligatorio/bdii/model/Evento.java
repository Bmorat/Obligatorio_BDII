package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @ToString
public class Evento {

    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer idEstadio;
    private String paisDocAdmin;
    private String tipoDocAdmin;
    private String numeroDocAdmin;

    public Evento() {}
}
