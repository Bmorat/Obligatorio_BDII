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
import java.time.LocalDate;

@Getter @Setter @ToString
@Entity
@Table(name = "Compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Column(name = "MontoTotal")
    private BigDecimal montoTotal;

    @Column(name = "PaisDocUsuario")
    private String paisDocUsuario;

    @Column(name = "TipoDocUsuario")
    private String tipoDocUsuario;

    @Column(name = "NumeroDocUsuario")
    private String numeroDocUsuario;

    @Column(name = "IdComision")
    private Integer idComision;

    public Compra() {}
}
