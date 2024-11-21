package com.apitv.proyecto.models.daos;

import com.apitv.proyecto.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo);
}