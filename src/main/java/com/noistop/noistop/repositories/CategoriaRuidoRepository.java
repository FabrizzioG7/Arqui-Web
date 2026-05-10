package com.noistop.noistop.repositories;

import com.noistop.noistop.entities.CategoriaRuido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoriaRuidoRepository extends JpaRepository<CategoriaRuido, Integer> {
    List<CategoriaRuido> findByNombreCategoriaContainingIgnoreCase(String nombre);
    Optional<CategoriaRuido> findByNombreCategoria(String nombre);
    boolean existsByNombreCategoria(String nombre);
    //CATEGORIA RUIDO REPO KENNETH
}
