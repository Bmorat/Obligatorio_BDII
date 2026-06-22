package com.obligatorio.bdii.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor
public class Estadio {

    private Integer id;
    private String nombre;
    private String ubicacion;
    private String paisDocAdmin;
    private String tipoDocAdmin;
    private String numeroDocAdmin;

  public Estadio (){}
}
