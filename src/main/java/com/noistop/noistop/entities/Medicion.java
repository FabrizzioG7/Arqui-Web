package com.noistop.noistop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Medicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_medicionid")
    private Integer pkMedicionId;

    @Column(name = "decibeles", nullable = false)
    private Integer decibeles;

    @Column(name = "fechahora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "medicion", fetch = FetchType.LAZY)
    private List<Reporte> reportes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
