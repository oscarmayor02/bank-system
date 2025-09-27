package org.clienteservice.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cliente extends Persona {

    @Column(unique = true, nullable = false)
    private String clienteId; // username interno

    @Email(message = "El email debe tener un formato válido")
    @Column(unique = true, nullable = false)
    private String email; // único

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasena; // se guarda hasheada con BCrypt

    private Boolean estado = true;
}

