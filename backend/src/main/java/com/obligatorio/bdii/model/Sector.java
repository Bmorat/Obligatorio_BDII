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

@Getter @Setter @ToString
@Entity
@Table(name = "Sector")
@IdClass(Sector.SectorId.class)
public class Sector {

    @Id
    @Column(name = "IdEstadio")
    private Integer idEstadio;

    @Id
    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Capacidad")
    private Integer capacidad;

    public Sector() {}

    @Getter @Setter
    @ToString
    public static class SectorId implements Serializable {
        private Integer idEstadio;
        private String tipo;

        public SectorId() {}

        public SectorId(Integer idEstadio, String tipo) {
            this.idEstadio = idEstadio;
            this.tipo = tipo;
        }
    }
}
