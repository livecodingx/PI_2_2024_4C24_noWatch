package com.apitv.proyecto.services;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.models.daos.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (usuarioRepositorio.existsById(id)) {
            usuarioRepositorio.deleteById(id);
        }
    }
    public void crearDatosFicticios() {
        if (usuarioRepositorio.count() == 0) {
            Usuario usuario1 = new Usuario();
            usuario1.setCorreo("juan.perez@example.com");
            usuario1.setNombre("Juan Perez");
            usuario1.setClave("clave123");
            usuarioRepositorio.save(usuario1);

            Usuario usuario2 = new Usuario();
            usuario2.setCorreo("maria.gomez@example.com");
            usuario2.setNombre("Maria Gomez");
            usuario2.setClave("clave456");
            usuarioRepositorio.save(usuario2);

            Usuario usuario3 = new Usuario();
            usuario3.setCorreo("carlos.lopez@example.com");
            usuario3.setNombre("Carlos Lopez");
            usuario3.setClave("clave789");
            usuarioRepositorio.save(usuario3);
        }
    }

}