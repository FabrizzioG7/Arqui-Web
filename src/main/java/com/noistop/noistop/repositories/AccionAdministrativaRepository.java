package com.noistop.noistop.repositories;

import com.noistop.noistop.dtos.AccionMensualDTO;
import com.noistop.noistop.entities.AccionAdministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccionAdministrativaRepository extends JpaRepository<AccionAdministrativa, Integer> {
    List<AccionAdministrativa> findByReportePkReporteId(Integer reporteId);
    List<AccionAdministrativa> findByUsuarioPkUsuarioId(Integer usuarioId);
    @Query("""
    SELECT new com.noistop.noistop.dtos.AccionMensualDTO(
        CAST(FUNCTION('TO_CHAR', a.fechaAccion, 'YYYY-MM') AS string),
        a.usuario.nombre,
        COUNT(a)
    )
    FROM AccionAdministrativa a
    GROUP BY FUNCTION('TO_CHAR', a.fechaAccion, 'YYYY-MM'), a.usuario.nombre
    ORDER BY FUNCTION('TO_CHAR', a.fechaAccion, 'YYYY-MM') ASC
    """)
    List<AccionMensualDTO> obtenerComparativaMensual();
}
