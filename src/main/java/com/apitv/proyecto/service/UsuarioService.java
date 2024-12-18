package com.apitv.proyecto.service;

import com.apitv.proyecto.models.entities.Usuario;
import com.apitv.proyecto.models.daos.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public UsuarioService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    public Long obtenerIdPorCorreo(String correo) {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        if (usuario != null) {
            return usuario.getId();
        }
        throw new RuntimeException("Usuario no encontrado con el correo: " + correo);
    }


    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }
}
