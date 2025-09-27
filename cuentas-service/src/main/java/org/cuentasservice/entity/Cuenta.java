package org.cuentasservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCuenta; // único por cliente

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String tipoCuenta; // Ahorros, Corriente

    @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
    private Double saldoInicial;

    private Boolean estado = true;

    // Guardamos referencia al cliente por ID
    @NotBlank(message = "El clienteId es obligatorio")
    private String clienteId;
}
