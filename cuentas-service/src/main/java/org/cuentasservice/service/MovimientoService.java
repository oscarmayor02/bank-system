package org.cuentasservice.service;

import org.cuentasservice.entity.Movimiento;

import java.util.List;

public interface MovimientoService {
    Movimiento registrarMovimiento(Long cuentaId, Movimiento movimiento);
    List<Movimiento> listarMovimientos(Long cuentaId);
    List<Movimiento> listarTodos();
}

