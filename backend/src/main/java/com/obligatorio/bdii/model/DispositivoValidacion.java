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

@Getter @Setter @ToString
@Entity
@Table(name = "Dispositivo_Validacion")
public class DispositivoValidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDispositivo")
    private Integer idDispositivo;

    @Column(name = "PaisDocFunc")
    private String paisDocFunc;

    @Column(name = "TipoDocFunc")
    private String tipoDocFunc;

    @Column(name = "NumeroDocFunc")
    private String numeroDocFunc;

    public DispositivoValidacion() {}
}
