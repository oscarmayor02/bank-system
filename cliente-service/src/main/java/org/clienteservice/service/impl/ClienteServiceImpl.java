package org.clienteservice.service.impl;

import org.clienteservice.entity.Cliente;
import org.clienteservice.exception.BusinessException;
import org.clienteservice.exception.DuplicateResourceException;
import org.clienteservice.exception.ResourceNotFoundException;
import org.clienteservice.repository.ClienteRepository;
import org.clienteservice.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail()))
            throw new DuplicateResourceException("El email ya está registrado");

        if (clienteRepository.existsByClienteId(cliente.getClienteId()))
            throw new DuplicateResourceException("El clienteId ya existe");

        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        existente.setNombre(cliente.getNombre());
        existente.setGenero(cliente.getGenero());
        existente.setEdad(cliente.getEdad());
        existente.setDireccion(cliente.getDireccion());
        existente.setTelefono(cliente.getTelefono());
        existente.setEmail(cliente.getEmail());
        existente.setEstado(cliente.getEstado());

        return clienteRepository.save(existente);
    }

    @Override
    public Cliente actualizarParcial(Long id, Map<String, Object> campos) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        campos.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Cliente.class, k);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, cliente, v);
            }
        });

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id))
            throw new ResourceNotFoundException("Cliente no encontrado");
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con email " + email + " no encontrado"));
    }
}


