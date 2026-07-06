package com.noistop.noistop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria_ruido")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CategoriaRuido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_categoriaid")
    private Integer pkCategoriaId;

    @Column(name = "nombrecategoria", nullable = false, length = 100, unique = true)
    private String nombreCategoria;

    @Column(name = "descripcioncategoria", length = 255)
    private String descripcionCategoria;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "categoriaRuido", fetch = FetchType.LAZY)
    private List<Reporte> reportes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
