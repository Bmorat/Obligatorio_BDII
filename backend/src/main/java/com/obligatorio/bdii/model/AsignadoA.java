package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class AsignadoA {

    private String paisDocFunc;
    private String tipoDocFunc;
    private String numeroDocFunc;
    private Integer idEvento;
    private Integer idEstadio;
    private String tipo;

    public AsignadoA() {}
}
