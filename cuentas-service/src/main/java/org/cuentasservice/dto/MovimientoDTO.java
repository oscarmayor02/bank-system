package org.cuentasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDTO {
    private LocalDate fecha;
    private String tipoMovimiento;
    private Double valor;
    private Double saldo;
}