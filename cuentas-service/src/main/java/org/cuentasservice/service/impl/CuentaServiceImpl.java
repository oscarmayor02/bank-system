package org.cuentasservice.service.impl;

import org.cuentasservice.dto.ClienteDTO;
import org.cuentasservice.entity.Cuenta;
import org.cuentasservice.exception.BusinessException;
import org.cuentasservice.feign.ClienteFeignClient;
import org.cuentasservice.repository.CuentaRepository;
import org.cuentasservice.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteFeignClient clienteFeignClient;

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {
        // Validamos que el cliente exista en clientes-service
        ClienteDTO cliente = clienteFeignClient.obtenerClientePorId(Long.parseLong(cuenta.getClienteId()));
        if (cliente == null || !cliente.getEstado()) {
            throw new BusinessException("El cliente no existe o está inactivo");
        }

        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta obtenerPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));
    }

    @Override
    public List<Cuenta> listarPorCliente(String clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }
}
