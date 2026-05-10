package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    List<Ubicacion> findByDistritoContainingIgnoreCase(String distrito);
}
