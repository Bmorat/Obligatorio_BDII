package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Estadio {

    private Integer id;
    private String nombre;
    private String ubicacion;

    public Estadio() {}
}
