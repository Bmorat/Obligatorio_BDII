package com.obligatorio.bdii.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Getter @Setter @ToString @AllArgsConstructor
public class SeHabilita {

    private Integer idEvento;
    private Integer idEstadio;
    private SectorTipo tipo;
    private BigDecimal precio;
    private Integer capacidadHabilitada;

    public SeHabilita() {}
}
