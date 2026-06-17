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
@Table(name = "Asignado_a")
@IdClass(AsignadoA.AsignadoAId.class)
public class AsignadoA {

    @Id
    @Column(name = "PaisDocFunc")
    private String paisDocFunc;

    @Id
    @Column(name = "TipoDocFunc")
    private String tipoDocFunc;

    @Id
    @Column(name = "NumeroDocFunc")
    private String numeroDocFunc;

    @Id
    @Column(name = "IdEvento")
    private Integer idEvento;

    @Id
    @Column(name = "IdEstadio")
    private Integer idEstadio;

    @Id
    @Column(name = "Tipo")
    private String tipo;

    public AsignadoA() {}

    @Getter @Setter
    @ToString
    public static class AsignadoAId implements Serializable {
        private String paisDocFunc;
        private String tipoDocFunc;
        private String numeroDocFunc;
        private Integer idEvento;
        private Integer idEstadio;
        private String tipo;

        public AsignadoAId() {}

        public AsignadoAId(String paisDocFunc, String tipoDocFunc, String numeroDocFunc, Integer idEvento, Integer idEstadio, String tipo) {
            this.paisDocFunc = paisDocFunc;
            this.tipoDocFunc = tipoDocFunc;
            this.numeroDocFunc = numeroDocFunc;
            this.idEvento = idEvento;
            this.idEstadio = idEstadio;
            this.tipo = tipo;
        }
    }
}
