package org.clienteservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clienteservice.entity.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCrearClienteEndpoint() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId("98");
        cliente.setNombre("Marianela");
        cliente.setIdentificacion("124434");
        cliente.setEdad(25);
        cliente.setEmail("marianelha@test.com");
        cliente.setContrasena("5678");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Marianela"));
    }

    @Test
    void testObtenerClienteNoExistente() throws Exception {
        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testActualizarCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Original");
        cliente.setIdentificacion("56478");
        cliente.setClienteId("98");
        cliente.setEdad(30);
        cliente.setEmail("juan3@test.com");
        cliente.setContrasena("1234");

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andReturn().getResponse().getContentAsString();

        Cliente creado = mapper.readValue(response, Cliente.class);

        creado.setNombre("Juan Modificado");

        mockMvc.perform(put("/api/clientes/" + creado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(creado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Modificado"));
    }
}