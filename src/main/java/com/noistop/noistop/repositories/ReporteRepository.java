package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import com.noistop.noistop.dtos.EstadoReporteCountDTO;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByUsuarioPkUsuarioId(Integer usuarioId);
    List<Reporte> findByCategoriaRuidoPkCategoriaId(Integer categoriaId);
    List<Reporte> findByUbicacionDistritoContainingIgnoreCase(String distrito);

    @Query("SELECT r FROM Reporte r WHERE r.usuario.pkUsuarioId = :usuarioId AND r.createdAt BETWEEN :inicio AND :fin")
    List<Reporte> findByUsuarioIdAndFechaRango(
            @Param("usuarioId") Integer usuarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT r.ubicacion.distrito, COUNT(r) as total FROM Reporte r GROUP BY r.ubicacion.distrito ORDER BY total DESC")
    List<Object[]> countReportesPorDistrito();

    @Query("SELECT new com.noistop.noistop.dtos.EstadoReporteCountDTO(r.estado, COUNT(r)) " +
            "FROM Reporte r GROUP BY r.estado ORDER BY COUNT(r) DESC")
    List<EstadoReporteCountDTO> countReportesPorEstado();
}
