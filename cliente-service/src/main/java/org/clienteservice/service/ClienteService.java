package org.clienteservice.service;

import org.clienteservice.entity.Cliente;

import java.util.List;
import java.util.Map;

public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, Cliente cliente);
    Cliente actualizarParcial(Long id, Map<String, Object> campos);
    void eliminarCliente(Long id);
    Cliente obtenerPorId(Long id);
    List<Cliente> listarClientes();
    Cliente obtenerPorEmail (String email);
}
