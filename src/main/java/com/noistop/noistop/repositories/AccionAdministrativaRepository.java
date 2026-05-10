package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.AccionAdministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccionAdministrativaRepository extends JpaRepository<AccionAdministrativa, Integer> {
    List<AccionAdministrativa> findByReportePkReporteId(Integer reporteId);
    List<AccionAdministrativa> findByUsuarioPkUsuarioId(Integer usuarioId);
}
