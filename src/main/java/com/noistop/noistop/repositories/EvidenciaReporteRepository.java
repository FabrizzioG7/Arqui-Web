package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.EvidenciaReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenciaReporteRepository extends JpaRepository<EvidenciaReporte, Integer> {
    List<EvidenciaReporte> findByReportePkReporteId(Integer reporteId);
}
