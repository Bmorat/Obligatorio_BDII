package com.obligatorio.bdii.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter @Setter @ToString
@Entity
@Table(name = "Se_habilita")
@IdClass(SeHabilita.SeHabilitaId.class)
public class SeHabilita {

    @Id
    @Column(name = "IdEvento")
    private Integer idEvento;

    @Id
    @Column(name = "IdEstadio")
    private Integer idEstadio;

    @Id
    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Precio")
    private BigDecimal precio;

    @Column(name = "CapacidadHabilitada")
    private Integer capacidadHabilitada;

    public SeHabilita() {}

    @Getter @Setter
    @ToString
    public static class SeHabilitaId implements Serializable {
        private Integer idEvento;
        private Integer idEstadio;
        private String tipo;

        public SeHabilitaId() {}

        public SeHabilitaId(Integer idEvento, Integer idEstadio, String tipo) {
            this.idEvento = idEvento;
            this.idEstadio = idEstadio;
            this.tipo = tipo;
        }
    }
}
