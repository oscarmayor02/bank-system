package org.clienteservice;

import lombok.extern.slf4j.Slf4j;
import org.clienteservice.entity.Cliente;
import org.clienteservice.repository.ClienteRepository;
import org.clienteservice.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@Transactional
class ClienteServiceTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testCrearClienteExitoso() {
        Cliente cliente = new Cliente();
        cliente.setClienteId("1");
        cliente.setNombre("Jose Lema");
        cliente.setIdentificacion("123456789");
        cliente.setEdad(25);
        cliente.setEmail("jose@test.com");
        cliente.setContrasena("1234");

        Cliente guardado = clienteService.crearCliente(cliente);

        assertNotNull(guardado.getId());
        assertEquals("Jose Lema", guardado.getNombre());
    }

    @Test
    void testNoPermiteMenoresDeEdad() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro Niño");
        cliente.setEdad(15);
        cliente.setEmail("pedro@test.com");
        cliente.setContrasena("abcd");

        assertThrows(Exception.class, () -> clienteService.crearCliente(cliente));
    }

    @Test
    void testEmailDebeSerUnico() {
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Cliente 1");
        cliente1.setEdad(30);
        cliente1.setEmail("repetido@test.com");
        cliente1.setContrasena("123");
        cliente1.setIdentificacion("123456789");
            cliente1.setClienteId("1");

        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Cliente 2");
        cliente2.setEdad(22);
        cliente2.setClienteId("2");
        cliente2.setIdentificacion("987654321");
        cliente2.setEmail("repetido@test.com");
        cliente2.setContrasena("456");

        clienteService.crearCliente(cliente1);
        assertThrows(Exception.class, () -> clienteService.crearCliente(cliente2));
    }
}