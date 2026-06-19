package com.obligatorio.bdii.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter @Setter @ToString
public class Transferencia {

    private Integer idTransferencia;
    private LocalDate fechaTransferencia;
    private String estadoTransferencia;
    private Integer idEntrada;
    private String paisDocUsuario;
    private String tipoDocUsuario;
    private String numeroDocUsuario;

    public Transferencia() {}
}
