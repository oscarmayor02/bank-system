package org.cuentasservice.service.impl;

import org.cuentasservice.entity.Cuenta;
import org.cuentasservice.entity.Movimiento;
import org.cuentasservice.exception.BusinessException;
import org.cuentasservice.repository.CuentaRepository;
import org.cuentasservice.repository.MovimientoRepository;
import org.cuentasservice.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public Movimiento registrarMovimiento(Long cuentaId, Movimiento movimiento) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        // Validamos depósito o retiro
        if ("Retiro".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            if (cuenta.getSaldoInicial() < movimiento.getValor()) {
                throw new BusinessException("Saldo no disponible");
            }
            cuenta.setSaldoInicial(cuenta.getSaldoInicial() - movimiento.getValor());
        } else if ("Depósito".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            cuenta.setSaldoInicial(cuenta.getSaldoInicial() + movimiento.getValor());
        } else {
            throw new BusinessException("Tipo de movimiento inválido");
        }

        movimiento.setSaldo(cuenta.getSaldoInicial());
        movimiento.setCuenta(cuenta);

        cuentaRepository.save(cuenta);
        return movimientoRepository.save(movimiento);
    }

    @Override
    public List<Movimiento> listarMovimientos(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }
}
