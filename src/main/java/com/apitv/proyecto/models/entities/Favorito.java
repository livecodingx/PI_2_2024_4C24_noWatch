package com.apitv.proyecto.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "favorito")
@Data
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_favorito")
    private Long id;

    @Column(name = "fk_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "url_canal", nullable = false)
    private String url;
}
