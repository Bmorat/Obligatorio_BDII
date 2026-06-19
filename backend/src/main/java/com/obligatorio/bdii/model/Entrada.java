package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @ToString
public class Entrada {

    private Integer idEntrada;
    private String estado;
    private Integer numeroVecesTransferida;
    private Integer idCompra;
    private Integer idEvento;
    private Integer idEstadio;
    private String tipo;
    private String idQR;
    private Integer idDispositivoValidacion;
    private String codigoAceptado;
    private LocalDateTime fechaHoraValidacion;

    public Entrada() {}
}
