package com.acessibilidade.api.repository;

import com.acessibilidade.api.model.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    Optional<UsuarioPerfil> findByEmail(String email);

    boolean existsByEmail(String email);
}