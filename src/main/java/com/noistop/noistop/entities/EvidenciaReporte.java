package com.noistop.noistop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidencia_reporte")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EvidenciaReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_evidenciaid")
    private Integer pkEvidenciaId;

    @Column(name = "rutaarchivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "nombre_original", length = 255)
    private String nombreOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reporteid", nullable = false)
    private Reporte reporte;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
