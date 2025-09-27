package org.cuentasservice.feign;

import org.cuentasservice.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes-service", url = "http://localhost:8081/api/clientes")
public interface ClienteFeignClient {

    // Consumimos endpoint de clientes-service para verificar que existe
    @GetMapping("/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable("id") Long id);
}
