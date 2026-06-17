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
@Table(name = "Comision")
public class Comision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdComision")
    private Integer idComision;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Column(name = "Porcentaje")
    private BigDecimal porcentaje;

    public Comision() {}
}
