package com.apitv.proyecto.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_usuario")
    private Long id;

    @Column(name = "correo_electronico", nullable = false, unique = true)
    private String correo;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombre;

    @Column(name = "apellido_usuario", nullable = false)
    private String apellido;

    @Column(name = "foto_url")
    private String fotoUrl;
}