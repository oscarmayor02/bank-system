package org.cuentasservice.controller;

import jakarta.validation.Valid;
import org.cuentasservice.entity.Cuenta;
import org.cuentasservice.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<Cuenta> crear(@Valid @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.crearCuenta(cuenta));
    }

    @GetMapping("/{numeroCuenta}")
    public Cuenta obtenerPorNumero(@PathVariable String numeroCuenta) {
        return cuentaService.obtenerPorNumero(numeroCuenta);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Cuenta> listarPorCliente(@PathVariable String clienteId) {
        return cuentaService.listarPorCliente(clienteId);
    }
}
