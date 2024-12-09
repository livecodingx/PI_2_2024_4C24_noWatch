package com.apitv.proyecto.repositories;

import com.apitv.proyecto.models.entities.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByIdUsuario(Long idUsuario);
}
