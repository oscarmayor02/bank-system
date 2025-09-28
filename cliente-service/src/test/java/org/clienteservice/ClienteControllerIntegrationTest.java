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
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCrearClienteEndpoint() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId("984");
        cliente.setNombre("Marianela");
        cliente.setIdentificacion("1244434");
        cliente.setEdad(25);
        cliente.setEmail("marianelha12@test.com");
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
        cliente.setIdentificacion("565478");
        cliente.setClienteId("986");
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

    @Test
    void testListarClientes() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testEliminarCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId("560");
        cliente.setNombre("EliminarViaController");
        cliente.setIdentificacion("777666555");
        cliente.setEdad(26);
        cliente.setEmail("deletecontroller1@test.com");
        cliente.setContrasena("pass");

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andReturn().getResponse().getContentAsString();

        Cliente creado = mapper.readValue(response, Cliente.class);

        mockMvc.perform(delete("/api/clientes/" + creado.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clientes/" + creado.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testActualizarParcialCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId("600");
        cliente.setNombre("Parcial");
        cliente.setIdentificacion("555444333");
        cliente.setEdad(27);
        cliente.setEmail("patc3h@test.com");
        cliente.setContrasena("pass");

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andReturn().getResponse().getContentAsString();

        Cliente creado = mapper.readValue(response, Cliente.class);

        String patchJson = """
        {"nombre":"ParcialModificado"}
    """;

        mockMvc.perform(patch("/api/clientes/" + creado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ParcialModificado"));
    }

    @Test
    void testCrearClienteConEmailInvalido() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId("70");
        cliente.setNombre("EmailInvalid");
        cliente.setIdentificacion("123123123");
        cliente.setEdad(25);
        cliente.setEmail("no-valido");
        cliente.setContrasena("12345");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearClienteMenorDeEdad() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId("80");
        cliente.setNombre("MenorDeEdad");
        cliente.setIdentificacion("321321321");
        cliente.setEdad(15);
        cliente.setEmail("menor@test.com");
        cliente.setContrasena("12345");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest());
    }
}