package org.cuentasservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReporteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGenerarReporteExitoso() throws Exception {
        mockMvc.perform(get("/api/reportes")
                        .param("clienteId", "1")
                        .param("fechaInicio", "2025-01-01")
                        .param("fechaFin", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente").value("1"))
                .andExpect(jsonPath("$.cuentas").isArray());
    }

    @Test
    void testGenerarReporteClienteSinCuentas() throws Exception {
        mockMvc.perform(get("/api/reportes")
                        .param("clienteId", "9999")
                        .param("fechaInicio", "2025-01-01")
                        .param("fechaFin", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuentas").isEmpty());
    }
}
