package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Juega {

    private Integer idEvento;
    private Integer idEquipo;
    private String rol;

    public Juega() {}
}
