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
@Table(name = "Juega")
@IdClass(Juega.JuegaId.class)
public class Juega {

    @Id
    @Column(name = "IdEvento")
    private Integer idEvento;

    @Id
    @Column(name = "IdEquipo")
    private Integer idEquipo;

    @Column(name = "Rol")
    private String rol;

    public Juega() {}

    @Getter @Setter
    @ToString
    public static class JuegaId implements Serializable {
        private Integer idEvento;
        private Integer idEquipo;

        public JuegaId() {}

        public JuegaId(Integer idEvento, Integer idEquipo) {
            this.idEvento = idEvento;
            this.idEquipo = idEquipo;
        }
    }
}
