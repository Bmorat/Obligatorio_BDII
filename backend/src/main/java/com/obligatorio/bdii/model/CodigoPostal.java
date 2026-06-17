package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CodigoPostal {

    private String codigo;
    private String localidad;
    private String pais;

    public CodigoPostal() {}
}
