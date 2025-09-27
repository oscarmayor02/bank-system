package org.cuentasservice.controller;

import jakarta.validation.Valid;
import org.cuentasservice.entity.Movimiento;
import org.cuentasservice.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/{cuentaId}")
    public ResponseEntity<Movimiento> registrar(@PathVariable Long cuentaId,
                                                @Valid @RequestBody Movimiento movimiento) {
        return ResponseEntity.ok(movimientoService.registrarMovimiento(cuentaId, movimiento));
    }

    @GetMapping("/{cuentaId}")
    public List<Movimiento> listar(@PathVariable Long cuentaId) {
        return movimientoService.listarMovimientos(cuentaId);
    }

    @GetMapping
    public List<Movimiento> listar() {
        return movimientoService.listarTodos();
    }
}
