package com.acessibilidade.api.repository;

import com.acessibilidade.api.model.AuditoriaPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaPerfilRepository extends JpaRepository<AuditoriaPerfil, Long> {
}