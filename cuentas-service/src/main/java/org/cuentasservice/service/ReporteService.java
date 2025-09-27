package org.cuentasservice.service;

import org.cuentasservice.dto.ReporteDTO;

import java.time.LocalDate;

public interface ReporteService {
    ReporteDTO generarReporte(String clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}
