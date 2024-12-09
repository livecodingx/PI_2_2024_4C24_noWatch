package com.apitv.proyecto.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "favoritos")
@Data
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "url", nullable = false)
    private String url;
}
