package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@Entity
@Table(name = "Entrada")
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEntrada")
    private Integer idEntrada;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "NumeroVecesTransferida")
    private Integer numeroVecesTransferida;

    @Column(name = "IdCompra")
    private Integer idCompra;

    @Column(name = "IdEvento")
    private Integer idEvento;

    @Column(name = "IdEstadio")
    private Integer idEstadio;

    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "IdQR")
    private String idQR;

    @Column(name = "IdDispositivoValidacion")
    private Integer idDispositivoValidacion;

    @Column(name = "CodigoAceptado")
    private String codigoAceptado;

    @Column(name = "FechaHoraValidacion")
    private LocalDateTime fechaHoraValidacion;

    public Entrada() {}
}
