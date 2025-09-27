package org.cuentasservice;

import org.cuentasservice.entity.Cuenta;
import org.cuentasservice.entity.Movimiento;
import org.cuentasservice.exception.BusinessException;
import org.cuentasservice.repository.CuentaRepository;
import org.cuentasservice.service.MovimientoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MovimientoServiceTest {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    void testRegistrarDepositoExitoso() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("12345");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(500.00);
        cuenta.setClienteId("1");
        cuenta.setEstado(true);
        cuentaRepository.save(cuenta);

        Movimiento deposito = new Movimiento();
        deposito.setCuenta(cuenta);
        deposito.setTipoMovimiento("Depósito");
        deposito.setValor(200.00);

        Movimiento registrado = movimientoService.registrarMovimiento(deposito.getCuenta().getId(), deposito);

        assertEquals(700.00, registrado.getSaldo());
    }

    @Test
    void testRegistrarRetiroConSaldoInsuficiente() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("99999");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(100.00);
        cuenta.setClienteId("1");
        cuentaRepository.save(cuenta);

        Movimiento retiro = new Movimiento();
        retiro.setCuenta(cuenta);
        retiro.setTipoMovimiento("RETIRO");
        retiro.setValor(200.00);

        assertThrows(BusinessException.class,
                () -> movimientoService.registrarMovimiento(retiro.getCuenta().getId(), retiro));
    }
}