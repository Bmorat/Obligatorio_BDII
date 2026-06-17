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
import java.time.LocalDate;

@Getter @Setter @ToString
@Entity
@Table(name = "Transferencia")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTransferencia")
    private Integer idTransferencia;

    @Column(name = "FechaTransferencia")
    private LocalDate fechaTransferencia;

    @Column(name = "EstadoTransferencia")
    private String estadoTransferencia;

    @Column(name = "IdEntrada")
    private Integer idEntrada;

    @Column(name = "PaisDocUsuario")
    private String paisDocUsuario;

    @Column(name = "TipoDocUsuario")
    private String tipoDocUsuario;

    @Column(name = "NumeroDocUsuario")
    private String numeroDocUsuario;

    public Transferencia() {}
}
