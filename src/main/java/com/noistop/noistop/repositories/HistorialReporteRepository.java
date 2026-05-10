package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.HistorialReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialReporteRepository extends JpaRepository<HistorialReporte, Integer> {
    List<HistorialReporte> findByReportePkReporteIdOrderByFechaCambioDesc(Integer reporteId);
}
