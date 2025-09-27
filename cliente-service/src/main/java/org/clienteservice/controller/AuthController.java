package org.clienteservice.controller;

import jakarta.validation.Valid;
import org.clienteservice.config.JwtUtil;
import org.clienteservice.dto.LoginRequest;
import org.clienteservice.dto.LoginResponse;
import org.clienteservice.entity.Cliente;
import org.clienteservice.exception.InvalidCredentialsException;
import org.clienteservice.exception.ResourceNotFoundException;
import org.clienteservice.repository.ClienteRepository;
import org.clienteservice.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el cliente con el email proporcionado"));

        if (!passwordEncoder.matches(request.getContrasena(), cliente.getContrasena())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generarToken(cliente.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
