package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @ToString
@Entity
@Table(name = "Evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Column(name = "Hora")
    private LocalTime hora;

    @Column(name = "IdEstadio")
    private Integer idEstadio;

    @Column(name = "PaisDocAdmin")
    private String paisDocAdmin;

    @Column(name = "TipoDocAdmin")
    private String tipoDocAdmin;

    @Column(name = "NumeroDocAdmin")
    private String numeroDocAdmin;

    public Evento() {}
}
