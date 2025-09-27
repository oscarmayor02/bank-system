package org.cuentasservice.service;

import org.cuentasservice.entity.Cuenta;

import java.util.List;

public interface CuentaService {
    Cuenta crearCuenta(Cuenta cuenta);
    Cuenta obtenerPorNumero(String numeroCuenta);
    List<Cuenta> listarPorCliente(String clienteId);
}
