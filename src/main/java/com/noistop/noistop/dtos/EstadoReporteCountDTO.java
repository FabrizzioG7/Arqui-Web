package com.noistop.noistop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoReporteCountDTO {
    private String estado;
    private Long cantidad;
}