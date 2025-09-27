package org.cuentasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaReporteDTO {
    private String numeroCuenta;
    private String tipo;
    private Double saldoInicial;
    private List<MovimientoDTO> movimientos;
}