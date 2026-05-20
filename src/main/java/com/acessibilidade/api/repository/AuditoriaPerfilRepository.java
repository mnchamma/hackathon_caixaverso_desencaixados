package com.acessibilidade.api.repository;

import com.acessibilidade.api.model.AuditoriaPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaPerfilRepository extends JpaRepository<AuditoriaPerfil, Long> {

    List<AuditoriaPerfil> findByUsuarioEmailOrderByDataAlteracaoDesc(String usuarioEmail);
}    
