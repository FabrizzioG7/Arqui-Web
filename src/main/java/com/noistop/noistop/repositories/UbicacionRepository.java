package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.Ubicacion;
import com.noistop.noistop.dtos.DistritoReporteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    List<Ubicacion> findByDistritoContainingIgnoreCase(String distrito);

    @Query("SELECT new com.noistop.noistop.dtos.DistritoReporteDTO(u.distrito, COUNT(r)) " +
           "FROM Ubicacion u JOIN u.reportes r " +
           "GROUP BY u.distrito " +
           "ORDER BY COUNT(r) DESC")
    List<DistritoReporteDTO> findDistritosConMasReportes();
}
