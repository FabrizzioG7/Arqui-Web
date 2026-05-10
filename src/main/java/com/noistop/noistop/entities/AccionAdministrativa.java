package com.noistop.noistop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accion_administrativa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AccionAdministrativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_accionid")
    private Integer pkAccionId;

    @Column(name = "detalle", nullable = false, length = 500)
    private String detalle;

    @Column(name = "fechaaccion")
    private LocalDateTime fechaAccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuarioid", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reporteid", nullable = false)
    private Reporte reporte;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.fechaAccion == null) {
            this.fechaAccion = LocalDateTime.now();
        }
    }
}
