package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Sector {

    private Integer idEstadio;
    private String tipo;
    private Integer capacidad;

    public Sector() {}
}
