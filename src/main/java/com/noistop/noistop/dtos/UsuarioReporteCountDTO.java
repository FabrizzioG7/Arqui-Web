package com.noistop.noistop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioReporteCountDTO {
    private String nombre;
    private Long cantidadReportes;
}