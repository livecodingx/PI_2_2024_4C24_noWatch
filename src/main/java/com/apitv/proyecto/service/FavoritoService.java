package com.apitv.proyecto.service;

import com.apitv.proyecto.models.entities.Favorito;
import com.apitv.proyecto.repositories.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    public List<Favorito> obtenerFavoritosPorUsuario(Long idUsuario) {
        return favoritoRepository.findByIdUsuario(idUsuario);
    }

    public boolean agregarFavorito(Long idUsuario, String url) {
        // Verifica si el canal ya está en favoritos
        List<Favorito> favoritosExistentes = favoritoRepository.findByIdUsuario(idUsuario);
        for (Favorito favorito : favoritosExistentes) {
            if (favorito.getUrl().equals(url)) {
                return false; // El canal ya está en favoritos
            }
        }

        // Si no está en favoritos, se guarda
        Favorito nuevoFavorito = new Favorito();
        nuevoFavorito.setIdUsuario(idUsuario);
        nuevoFavorito.setUrl(url);
        favoritoRepository.save(nuevoFavorito);
        return true;
    }

    public void eliminarFavorito(Long id) {
        favoritoRepository.deleteById(id);
    }
}
