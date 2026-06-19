package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DispositivoValidacion {

    private Integer idDispositivo;
    private String paisDocFunc;
    private String tipoDocFunc;
    private String numeroDocFunc;

    public DispositivoValidacion() {}
}
