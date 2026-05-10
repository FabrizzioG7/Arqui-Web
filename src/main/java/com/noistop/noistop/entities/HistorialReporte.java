package com.noistop.noistop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_reporte")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HistorialReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_historialid")
    private Integer pkHistorialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reporteid", nullable = false)
    private Reporte reporte;

    @Column(name = "estado_anterior", length = 50)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", length = 50)
    private String estadoNuevo;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;

    @PrePersist
    protected void onCreate() {
        this.fechaCambio = LocalDateTime.now();
    }
}
