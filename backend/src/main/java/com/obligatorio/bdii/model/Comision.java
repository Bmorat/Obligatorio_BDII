package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @ToString
public class Comision {

    private Integer idComision;
    private String estado;
    private LocalDate fecha;
    private BigDecimal porcentaje;

    public Comision() {}
}
