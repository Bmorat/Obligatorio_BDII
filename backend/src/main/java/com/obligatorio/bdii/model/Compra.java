package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @ToString
public class Compra {

    private Integer id;
    private String estado;
    private LocalDate fecha;
    private BigDecimal montoTotal;
    private String paisDocUsuario;
    private String tipoDocUsuario;
    private String numeroDocUsuario;
    private Integer idComision;

    public Compra() {}
}
