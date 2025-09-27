package org.cuentasservice.service.impl;


import lombok.RequiredArgsConstructor;
import org.cuentasservice.dto.CuentaReporteDTO;
import org.cuentasservice.dto.MovimientoDTO;
import org.cuentasservice.dto.ReporteDTO;
import org.cuentasservice.entity.Cuenta;
import org.cuentasservice.entity.Movimiento;
import org.cuentasservice.repository.CuentaRepository;
import org.cuentasservice.repository.MovimientoRepository;
import org.cuentasservice.service.ReporteService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    public ReporteDTO generarReporte(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        List<CuentaReporteDTO> cuentasReporte = cuentas.stream().map(cuenta -> {
            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                    cuenta.getId(), fechaInicio, fechaFin);

            List<MovimientoDTO> movimientosDTO = movimientos.stream()
                    .map(m -> new MovimientoDTO(m.getFecha(), m.getTipoMovimiento(), m.getValor(), m.getSaldo()))
                    .collect(Collectors.toList());

            return new CuentaReporteDTO(
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta(),
                    cuenta.getSaldoInicial(),
                    movimientosDTO
            );
        }).collect(Collectors.toList());

        return new ReporteDTO(clienteId.toString(), fechaInicio, fechaFin, cuentasReporte);
    }
}
